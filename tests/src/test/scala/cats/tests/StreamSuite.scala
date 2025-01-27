package cats.tests

import cats._
import cats.data.ZipStream
import cats.laws.discipline._
import cats.laws.discipline.arbitrary._
import cats.syntax.all._
import org.scalacheck.Prop._

@annotation.nowarn("cat=deprecation")
class StreamSuite extends CatsSuite {
  checkAll("Stream[Int]", SemigroupalTests[Stream].semigroupal[Int, Int, Int])
  checkAll("Semigroupal[Stream]", SerializableTests.serializable(Semigroupal[Stream]))

  checkAll("Stream[Int]", CoflatMapTests[Stream].coflatMap[Int, Int, Int])
  checkAll("CoflatMap[Stream]", SerializableTests.serializable(CoflatMap[Stream]))

  checkAll("Stream[Int]", AlternativeTests[Stream].alternative[Int, Int, Int])
  checkAll("Alternative[Stream]", SerializableTests.serializable(Alternative[Stream]))

  checkAll("Stream[Int]", MonadTests[Stream].monad[Int, Int, Int])
  checkAll("Monad[Stream]", SerializableTests.serializable(Monad[Stream]))

  checkAll("Stream[Int] with Option", TraverseTests[Stream].traverse[Int, Int, Int, Set[Int], Option, Option])
  checkAll("Traverse[Stream]", SerializableTests.serializable(Traverse[Stream]))

  checkAll("Stream[Int]", TraverseFilterTests[Stream].traverseFilter[Int, Int, Int])
  checkAll("TraverseFilter[Stream]", SerializableTests.serializable(TraverseFilter[Stream]))

  checkAll("Stream[Int]", AlignTests[Stream].align[Int, Int, Int, Int])
  checkAll("Align[Stream]", SerializableTests.serializable(Align[Stream]))

  checkAll("Stream[Int]", ShortCircuitingTests[Stream].foldable[Int])
  checkAll("Stream[Int]", ShortCircuitingTests[Stream].traverseFilter[Int])

  // Can't test applicative laws as they don't terminate
  checkAll("ZipStream[Int]", CommutativeApplyTests[ZipStream].apply[Int, Int, Int])

  test("show") {
    assert(Stream(1, 2, 3).show === s"Stream(1, ?)")
    assert(Stream.empty[Int].show === s"Stream()")
  }

  test("Show[Stream] is referentially transparent, unlike Stream.toString") {
    forAll { (stream: Stream[Int]) =>
      if (!stream.isEmpty) {
        val unevaluatedLL = stream.map(identity)
        val initialShow = unevaluatedLL.show

        // Evaluating the tail can cause Stream.toString to return different values,
        // depending on the internal state of the Stream. Show[Stream] should return
        // consistent values independent of internal state.
        unevaluatedLL.tail
        assert(initialShow === (unevaluatedLL.show))
      } else {
        assert(stream.show === (stream.toString))
      }
    }
  }

  test("NonEmptyParallel instance in cats.instances.stream") {
    forAll { (s1: Stream[Int], s2: Stream[String]) =>
      assert((s1, s2).parTupled === s1.zip(s2))
    }
  }
}
