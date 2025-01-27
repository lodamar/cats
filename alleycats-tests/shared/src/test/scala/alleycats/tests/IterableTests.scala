package alleycats.tests

import alleycats.std.all._
import cats.{Eval, Foldable}
import cats.instances.all._
import cats.laws.discipline.{TraverseFilterTests, TraverseTests}

class IterableTests extends AlleycatsSuite {

  test("foldLeft sum == sum") {
    val it = Iterable(1, 2, 3)
    assertEquals(Foldable[Iterable].foldLeft(it, 0) { case (b, a) =>
                   a + b
                 },
                 it.sum
    )
  }

  test("foldRight early termination") {
    assertEquals(Foldable[Iterable]
                   .foldRight(Iterable(1, 2, 3), Eval.now("KO")) {
                     case (2, _) => Eval.now("OK")
                     case (a, b) => b
                   }
                   .value,
                 Eval.now("OK").value
    )
  }

  checkAll("Traverse[Iterable]", TraverseTests[Iterable].traverse[Int, Int, Int, Set[Int], Option, Option])

  checkAll("TraverseFilter[Iterable]", TraverseFilterTests[Iterable].traverseFilter[Int, Int, Int])
}
