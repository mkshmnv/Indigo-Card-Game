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

enum class Decks() {
    DECK,
    TABLE,
    PLAYER,
    COMPUTER
}

enum class Turn() {
    PLAYER,
    COMPUTER
}

class Card(val rank: Ranks, val suit: Suits) {
    override fun toString() = "${rank.symbol}${suit.symbol}"
}


fun main() {
    val deck = createDeck()
    val table = initialTable(deck)
    val playerHand = mutableListOf<Card>()
    val computerHand = mutableListOf<Card>()





//    startGame()
}

fun startGame(): List<Pair<Decks, MutableList<Card>>> {
    var whoseTurn = if (firstTurn()) Turn.PLAYER else Turn.COMPUTER

    val allDecks = mutableListOf<Pair<Decks, MutableList<Card>>>()

    val deck = createDeck()
    val table = initialTable(deck)


    allDecks.add(Decks.DECK to deck)
    deck.drop(4)
    allDecks.add(Decks.TABLE to table)
    allDecks.add(Decks.PLAYER to createDeck())
    allDecks.add(Decks.DECK to playerHand())
    return listOf(Decks.DECK to computerHand())
}

fun initialTable(deck: MutableList<Card>): MutableList<Card> {
    val table = deck.subList(0, 4)
    println("Initial cards on the table: ${table.joinToString(" ")}")
    return table
}

fun firstTurn(): Boolean {
    println("Indigo Card Game\nPlay first?")
    return readln() == "yes"
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

fun createDeck(): MutableList<Card> {
    val deck = mutableListOf<Card>()
    Suits.values().forEach { suit ->
        Ranks.values().forEach { rank ->
            deck.add(Card(rank, suit))
        }
    }
    deck.shuffle()
    return deck
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
