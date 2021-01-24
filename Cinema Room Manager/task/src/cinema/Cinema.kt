package cinema

data class Cinema(val row: Int, val col: Int) {
    data class Seat(var buy: Boolean, val price: Int)

    val data = List(row) { i ->
        List(col) {
            Seat(false, if (row * col <= 60) 10 else if (i < row / 2) 10 else 8)
        }
    }

    fun getPrice(i: Int, j: Int) = data[i - 1][j - 1].price

    fun isFree(seatRow: Int, seatCol: Int) =
        !data[seatRow - 1][seatCol - 1].buy


    fun buySeat(seatRow: Int, seatCol: Int) {
        data[seatRow - 1][seatCol - 1].buy = true
    }

    fun printStat() {
        print("Number of purchased tickets: ")
        println(data.flatten().count { it.buy })

        print("Percentage: ")
        println("%.2f%%".format(data.flatten().count { it.buy }.toDouble() / data.flatten().count() * 100))

        print("Current income: $")
        println(data.flatten().fold(0) { acc, seat -> acc + if (seat.buy) seat.price else 0 })

        print("Total income: $")
        println(data.flatten().sumBy { it.price })

        println()
    }

    override fun toString(): String {
        var s = "\nCinema:\n "
        repeat(col) { s += " ${it + 1}" }
        s += "\n"
        data.forEachIndexed { i, list -> s += "${i + 1} " + list.joinToString(" ") { if (it.buy) "B" else "S" } + "\n" }
        return s
    }
}

fun main() {
    println("Enter the number of rows:")
    val row = readLine()!!.toInt()
    println("Enter the number of seats in each row:")
    val col = readLine()!!.toInt()
    val cinema = Cinema(row, col)
    while (true) {
        println("1. Show the seats\n2. Buy a ticket\n3. Statistics\n0. Exit")
        when (readLine()!!.toInt()) {
            1 -> println(cinema)
            2 -> while (true) {
                println("Enter a row number:")
                val seatRow = readLine()!!.toInt()
                println("Enter a seat number in that row:")
                val seatCol = readLine()!!.toInt()
                when {
                    !(seatRow in 1..cinema.row && seatCol in 1..cinema.col) -> println("Wrong input!")
                    !(cinema.isFree(seatRow, seatCol)) -> println("That ticket has already been purchased")
                    else -> {
                        println("Ticket price: $" + cinema.getPrice(seatRow, seatCol))
                        cinema.buySeat(seatRow, seatCol)
                        break
                    }
                }
            }
            3 -> cinema.printStat()
            0 -> return
        }
    }
}