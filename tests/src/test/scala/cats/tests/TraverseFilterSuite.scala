package cats.tests

import cats.Traverse
import cats.TraverseFilter
import cats.data.Chain
import cats.laws.discipline.arbitrary.catsLawsArbitraryForChain
import cats.syntax.all._
import org.scalacheck.Arbitrary
import org.scalacheck.Prop.forAll

import scala.collection.immutable.Queue

abstract class TraverseFilterSuite[F[_]: TraverseFilter](name: String)(implicit
  ArbFInt: Arbitrary[F[Int]],
  ArbFString: Arbitrary[F[String]]
) extends CatsSuite {

  implicit def T: Traverse[F] = implicitly[TraverseFilter[F]].traverse

  test(s"TraverseFilter[$name].ordDistinct") {
    forAll { (fa: F[Int]) =>
      fa.ordDistinct.toList === fa.toList.distinct
    }
  }

  test(s"TraverseFilter[$name].hashDistinct") {
    forAll { (fa: F[String]) =>
      fa.hashDistinct.toList === fa.toList.distinct
    }
  }
}

class TraverseFilterListSuite extends TraverseFilterSuite[List]("list")

class TraverseFilterVectorSuite extends TraverseFilterSuite[Vector]("vector")

class TraverseFilterChainSuite extends TraverseFilterSuite[Chain]("chain")

class TraverseFilterQueueSuite extends TraverseFilterSuite[Queue]("queue")

@annotation.nowarn("cat=deprecation")
class TraverseFilterStreamSuite extends TraverseFilterSuite[Stream]("stream")
