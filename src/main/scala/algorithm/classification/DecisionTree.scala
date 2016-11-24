// Wei Chen - Decision Tree
// 2016-11-24

package ght.mi.algorithm
import ght.mi.algorithm.MatrixFunc._

class DecisionNode(col: Int, v: Double, tnode: DecisionNode , fnode: DecisionNode, r: Map[Int, Int]) {
    val column: Int = col
    val value: Double = v
    val trueNode: DecisionNode = tnode
    val falseNode: DecisionNode = fnode
    val results: Map[Int, Int] = r

    def predict(x: Array[Double]): Int = {
        if (results == null) {
            if (x(column) >= value) return trueNode.predict(x)
            else return falseNode.predict(x)
        } else return results.maxBy(_._2)._1
    }
}

class DecisionTree() {

    var tree: DecisionNode = null
    def clear() = tree = null

    private def log2(x: Double) = Math.log(x) / Math.log(2)

    private def uniqueCount(data: Array[(Int, Array[Double])]): Map[Int, Int] = {
        return data.groupBy(_._1).map(t => (t._1, t._2.size))
    }

    private def entropy(data: Array[(Int, Array[Double])]): Double = {
        val dataSize = data.size.toDouble
        return uniqueCount(data).map { case (k, v) =>
            val p = v / dataSize
            -p * log2(p)
        }.sum
    }

    private def buildtree(data: Array[(Int, Array[Double])]): DecisionNode = {
        var currentScore: Double = entropy(data)
        var bestGain: Double = 0
        var bestColumn: Int = 0
        var bestValue: Double = 0
        var bestTrueData = Array[(Int, Array[Double])]()
        var bestFalseData = Array[(Int, Array[Double])]()

        val dataSize = data.size.toDouble
        val columnSize: Int = data.head._2.size
        for (col <- 0 until columnSize) {
            var valueSet: Set[Double] = Set()
            for (d <- data) valueSet += d._2(col)
            for (value <- valueSet) {
                val (tData, fData) = data.partition(d => d._2(col) >= value)
                val p = tData.size / dataSize
                val gain = currentScore - p * entropy(tData) - (1-p) * entropy(fData)
                if (gain > bestGain && tData.size > 0 && fData.size > 0) {
                    bestGain = gain
                    bestColumn = col
                    bestValue = value
                    bestTrueData = tData
                    bestFalseData = fData
                }
            }
        }
        if (bestGain > 0) {
            val tnode = buildtree(bestTrueData)
            val fnode = buildtree(bestFalseData)
            return new DecisionNode(bestColumn, bestValue, tnode, fnode, null)
        } else return new DecisionNode(0, 0, null, null, uniqueCount(data))
    }

    def train(data: Array[(Int, Array[Double])]) = tree = buildtree(data)

    def predict(x: Array[Array[Double]]): Array[Int] = x.map(xi => tree.predict(xi))
}
