// Wei Chen - Alex Rodriquez Fast Cluster Science 2014
// 2015-07-08

package ght.mi.algorithm
class DensityPeakCluster() {

    // Density Delta Data
    var dddata = Array[(Double, Double, Int)]()
    def clear() = dddata = Array[(Double, Double, Int)]()

    // Density Delta export import
    def importdd(data: Array[(Double, Double, Int)]) = dddata = data

    // density(Array(Array(1.0,2.0), Array(2.0,2.0),
    //   Array(1.0,0.1), Array(0.0,0.0)))
    def density(
        data: Array[Array[Double]]
    ) = {
        val densitydata = data.zipWithIndex.map { l1 =>
            (l1._2, data.map { l2 =>
                Math.exp(-l1._1.zip(l2).map { l3 =>
                    Math.pow(l3._1-l3._2, 2)
                }.sum)
            }.sum, l1._1)
        }
        dddata = densitydata.map { l1 =>
            val t = l1._3
            val ld = densitydata.filter(_._2 > l1._2).map { l2 =>
                (Math.sqrt(t.zip(l2._3).map { l3 =>
                    Math.pow(l3._1-l3._2, 2)
                }.sum), l2._1)
            }
            if (ld.size > 0) {
                val ldmin = ld.minBy(_._1)
                (l1._2, ldmin._1, ldmin._2)
            } else (l1._2, t.size.toDouble, -1)
        }
    }

    // cluster(0.8, 1.1)
    def cluster(
        densityf: Double,
        deltaf: Double
    ):Array[Int] = {
        val densitydelta = dddata.zipWithIndex.map { l =>
            (l._2, l._1._1, l._1._2, l._1._3)
        }.sortBy(l => l._2).reverse
        var groupdata = Array[(Int, Int, Int)]()
        var group = 0
        for (l <- densitydelta) {
            if (l._2 > densityf && l._3 > deltaf) {
                group += 1
                groupdata :+= (l._1, l._4, group)
            }else groupdata :+= (l._1, l._4, 0)
        }
        var groupkeymap = groupdata.map(l => (l._1, l._3)).toMap
        for (l <- groupdata) {
            if (l._3 == 0) groupkeymap += (l._1 -> groupkeymap(l._2))
        }
        return groupkeymap.toArray.sortBy(_._1).map(_._2)
    }
}

// var dc = DensityCluster
// dc.density(Array(Array(0.0,1.0), Array(1.0,1.0), Array(0.0,-1.1), Array(-1.0,-1.0)))
// dc.cluster(0.8,1.1)
