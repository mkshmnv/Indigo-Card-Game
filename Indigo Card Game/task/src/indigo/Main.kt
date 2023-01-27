package indigo

val DECK = mutableListOf<Card>()

enum class Suits(val symbol: String) {
    SPADES("♠"),
    HEARD("♥"),
    DIAMONDS("♦"),
    CLUBS("♣");

    companion object {
        override fun toString() = values().joinToString(" ") { it.symbol }
    }
}

enum class Ranks(val symbol: String) {
    ACE("A"),
    TWO("2"),
    THREE("3"),
    FOUR("4"),
    FIVE("5"),
    SIX("6"),
    SEVEN("7"),
    EIGHT("8"),
    NINE("9"),
    TEN("10"),
    JACK("J"),
    QUEEN("Q"),
    KING("K");

    companion object {
        override fun toString() = values().joinToString(" ") { it.symbol }
    }
}

class Card(val rank: Ranks, val suit: Suits) {
    override fun toString() = "${rank.symbol}${suit.symbol}"
}

fun main() {
    chooseAnAction()

}

fun chooseAnAction() {
    println("Choose an action (reset, shuffle, get, exit):")
    when (readln()) {
        "reset" -> reset()
        "shuffle" -> shuffle()
        "get" -> get()
        "exit" -> bye()
        else -> wrongAction()
    }
}

fun reset() {
    Suits.values().forEach { suit ->
        Ranks.values().forEach { rank ->
            DECK.add(Card(rank, suit))
        }
    }
    println("Card deck is reset.")
    // Test deck
    testDeckPrint()
    chooseAnAction()
}

fun shuffle() {
    DECK.shuffle()
    println("Card deck is shuffled.")
    // Test deck
    testDeckPrint()
    chooseAnAction()
}

fun get() {
    println("Number of cards:")
    val numberOfCards = readln().toIntOrNull()
    println(
        when (numberOfCards) {
            !in 1..52 -> "Invalid number of cards."
            null -> "Null error"
            else -> "Error"
        }
    )

    chooseAnAction()
}

fun wrongAction() {
    println("Wrong action.")
    chooseAnAction()
}

fun bye() {
    println("Bye")
}

fun testDeckPrint() {
    println("<TEST DECK PRINT")
    DECK.forEach { print("$it ") }
    println("\nTEST>")
}
