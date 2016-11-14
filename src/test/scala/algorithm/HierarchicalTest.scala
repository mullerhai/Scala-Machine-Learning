// Wei Chen - Hierarchical Test
// 2016-11-12

import org.scalatest.FunSuite
import ght.mi.algorithm.MatrixFunc._
import ght.mi.algorithm.Hierarchical

class HierarchicalSuite extends FunSuite {
    val data = Array(
        Array(1.0, 2.0), Array(1.0, 1.0),
        Array(0.8, 1.0), Array(2.0, 3.0),
        Array(1.1, 1.1), Array(2.0, 2.2),
        Array(6.0, 5.0), Array(6.0, 7.0),
        Array(6.0, 6.6), Array(6.0, 6.1),
        Array(6.0, 6.2), Array(6.0, 7.2)
    )

    val hi = new Hierarchical()
    test("Hierarchical Test : Initialization") {
        val result = hi.cluster(Array(Array(1.0),Array(-1.0)), 2, 1)
        assert(arrayequal(result.map(_.toDouble), Array(1,2)))
    }

    test("Hierarchical Test : Clustering") {
        val result = hi.cluster(data, 2, 3)
        assert(arrayequal(result.map(_.toDouble),
            Array(1,1,1,1,1,1,2,2,2,2,2,2)))
    }
}