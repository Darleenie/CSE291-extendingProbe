import ast.{ASTNode, IntAddition, IntLiteral, IntNode, IntVariable, Types}
import enumeration.{ChildrenIterator, ProbChildrenIterator}
import org.junit.Test
import org.scalatestplus.junit.JUnitSuite
import org.junit.Assert._


class ChildrenIteratorsTests extends JUnitSuite {

  @Test def pairsIterator(): Unit = {
    val nodes = List(new IntLiteral(1,1), new IntLiteral(2,1), new IntLiteral(3,1))
    val chit = new ChildrenIterator(nodes,List(Types.Int,Types.Int),1)
    assertTrue(chit.hasNext)
    assertEquals(List("1","1"), chit.next().map(_.code))
    assertEquals(List("1","2"), chit.next().map(_.code))
    assertEquals(List("1","3"), chit.next().map(_.code))
    assertEquals(List("2","1"), chit.next().map(_.code))
    assertEquals(List("2","2"), chit.next().map(_.code))
    assertEquals(List("2","3"), chit.next().map(_.code))
    assertEquals(List("3","1"), chit.next().map(_.code))
    assertEquals(List("3","2"), chit.next().map(_.code))
    assertEquals(List("3","3"), chit.next().map(_.code))
    assertFalse(chit.hasNext)
  }

  @Test def onesIterator(): Unit = {
    //Limit by height
    val nodes = List(new IntLiteral(1,1), new IntLiteral(2,1), new IntLiteral(3,1), new IntAddition(new IntVariable("x",Map("x" -> 0) :: Nil), new IntLiteral(1,1)))
    val chit = new ChildrenIterator(nodes,List(Types.Int),2)
    assertTrue(chit.hasNext)
    assertEquals(List("(+ x 1)"), chit.next().map(_.code))
    assertFalse(chit.hasNext)
  }

  @Test def pairsHeightFiltered(): Unit = {
    val nodes = List(new IntLiteral(1,1), new IntLiteral(2,1), new IntLiteral(3,1), new IntAddition(new IntVariable("x",Map("x" -> 0) :: Nil), new IntLiteral(1,1)))
    val chit = new ChildrenIterator(nodes,List(Types.Int,Types.Int),2)
    assertTrue(chit.hasNext)
    assertEquals(List("1","(+ x 1)"), chit.next().map(_.code))
    assertEquals(List("2","(+ x 1)"), chit.next().map(_.code))
    assertEquals(List("3","(+ x 1)"), chit.next().map(_.code))
    assertEquals(List("(+ x 1)","1"), chit.next().map(_.code))
    assertEquals(List("(+ x 1)","2"), chit.next().map(_.code))
    assertEquals(List("(+ x 1)","3"), chit.next().map(_.code))
    assertEquals(List("(+ x 1)","(+ x 1)"), chit.next().map(_.code))
    assertFalse(chit.hasNext)
  }

  @Test def costRollingFourChildren: Unit = {
    val nodes = List(
      new IntNode {
        override val values: List[Int] = List(0)
        override val code: String = "0"
        override val height: Int = 0
        override val terms: Int = 1
        override val children: Iterable[ASTNode] = Nil

        override def includes(varName: String): Boolean = false

        override def cost: Int = 1
      }, new IntNode {
        override val values: List[Int] = List(1)
        override val code: String = "1"
        override val height: Int = 0
        override val terms: Int = 1
        override val children: Iterable[ASTNode] = Nil

        override def includes(varName: String): Boolean = false

        override def cost: Int = 1
      }, new IntNode {
        override val values: List[Int] = List(2)
        override val code: String = "x"
        override val height: Int = 0
        override val terms: Int = 1
        override val children: Iterable[ASTNode] = Nil

        override def includes(varName: String): Boolean = false

        override def cost: Int = 1
      }
    )
  }

}
