package indigo

val DECK = mutableListOf<Card>()
val TABLE = mutableListOf<Card>()
val PLAYER = mutableListOf<Card>()
val COMPUTER = mutableListOf<Card>()

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

enum class Decks(var deck: MutableList<Card>) {
    DECK(mutableListOf()),
    TABLE(mutableListOf()),
    PLAYER(mutableListOf()),
    COMPUTER(mutableListOf())
}

enum class Turn {
    PLAYER,
    COMPUTER
}

class Card(val rank: Ranks, val suit: Suits) {
    override fun toString() = "${rank.symbol}${suit.symbol}"
}


fun main() {
    startGame()
}

fun startGame() {
    println("Indigo Card Game\nPlay first?")
    firstTurn()
    createDeck()
}

fun firstTurn() {
    when (readln()) {
        "yes" -> Turn.PLAYER
        "no" -> Turn.COMPUTER
        else -> {
            println("Incorrect choice, please enter \"yes\" or \"no\"")
            firstTurn()
        }
    }
}

fun createDeck() {
    Suits.values().forEach { suit ->
        Ranks.values().forEach { rank ->
            Decks.DECK.deck.add(Card(rank, suit))
        }
    }
    Decks.DECK.deck.shuffle()
}

fun initialTable(): MutableList<Card> {
    Decks.TABLE.deck = Decks.DECK.deck.subList(0, 4)
    Decks.DECK.deck.drop(4)

    println("Initial cards on the table: ${table.joinToString(" ")}")

    return table
}


fun chooseAnAction() {
    // Test deck
//    testDeckPrint()
    println("Choose an action (reset, shuffle, get, exit):")
    when (readln()) {
        "reset" -> createDeck()
        "shuffle" -> shuffle()
        "get" -> get()
        "exit" -> bye()
        else -> wrongAction()
    }
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
