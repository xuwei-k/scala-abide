package scala.tools.abide
package rules

class MatchCaseOnSeqTest extends AnalysisTest {
  import scala.tools.abide.traversal._

  analyzer.enableOnly("match-case-on-seq")

  "Seqs" should "not be matched with ::" in {
    val tree = fromString("""
      class Toto {
        def test(list : Seq[Int]) = list match {
          case x :: xs => true
          case _ => false
        }
      }
    """)

    global.ask { () => analyzer(tree).size should be (1) }
  }

  it should "be matched with Nil" in {
    val tree = fromString("""
      class Toto {
        def toto(list : Seq[Int]) = list match {
          case Nil => true
          case _ => false
        }
      }
    """)

    global.ask { () => analyzer(tree).isEmpty should be (true) }
  }

  it should "not be matched by :: (even when Nil is around)" in {
    val tree = fromString("""
      class Toto {
        def toto(list : Seq[Int]) = list match {
          case x :: xs => true
          case Nil => false
        }
      }
    """)

    global.ask { () => analyzer(tree).size should be (1) }
  }

  it should "work fine on other matchers" in {
    val tree = fromString("""
      class Toto {
        def toto(list : Seq[Int]) = list match {
          case Seq(a, b) => true
          case _ => false
        }
      }
    """)

    global.ask { () => analyzer(tree).isEmpty should be (true) }
  }

  "Lists" should "accept :: and Nil as matchers" in {
    val tree = fromString("""
      class Toto {
        def toto(list : List[Int]) = list match {
          case x :: xs => true
          case Nil => false
        }
      }
    """)

    global.ask { () => analyzer(tree).isEmpty should be (true) }
  }

}
