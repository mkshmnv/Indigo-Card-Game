package indigo

val DECK = mutableListOf<Card>()

enum class Suits(val symbol: String) {
    SPADES("♠"),
    HEARD("♥"),
    DIAMONDS("♦"),
    CLUBS("♣")
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
    KING("K")
}

class Card(val rank: Ranks, val suit: Suits) {
    override fun toString() = "${rank.symbol}${suit.symbol}"
}

fun main() {
    chooseAnAction()
}

fun chooseAnAction() {
    // Test deck
//    testDeckPrint()
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
    DECK.clear()
    Suits.values().forEach { suit ->
        Ranks.values().forEach { rank ->
            DECK.add(Card(rank, suit))
        }
    }
    println("Card deck is reset.")
    chooseAnAction()
}

fun shuffle() {
    DECK.shuffle()
    println("Card deck is shuffled.")
    chooseAnAction()
}

fun get() {
    println("Number of cards:")
    when (val numberOfCards = readln().toIntOrNull()) {
        !in 1..52, null -> {
            println("Invalid number of cards.")
        }
        !in 1..DECK.size -> {
            println("The remaining cards are insufficient to meet the request.")
        }
        else -> {
            val removedCard = DECK.drop(numberOfCards)
            println(DECK.filter { it !in removedCard }.joinToString(" "))
            DECK.removeAll { it !in removedCard }
        }
    }
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
    println(
        """>    START TEST DECK PRINT
        |${DECK.size}
        |${DECK.joinToString(" ")}
        |   FINISH TEST<""".trimMargin()
    )
}
