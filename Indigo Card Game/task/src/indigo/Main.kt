package indigo


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
    start()
    game()
}

fun game() {
    while (Decks.DECK.deck.size > 0) {
        println("${Decks.TABLE.deck.size} cards on the table, and the top card is ${Decks.TABLE.deck.first()}")

    }
}

fun start() {
    println("Indigo Card Game\nPlay first?")
    firstTurn()
    createDeck()
    initialTable()
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

fun initialTable() {
    Decks.TABLE.deck = Decks.DECK.deck.subList(0, 4)
    Decks.DECK.deck.drop(4)
    println("Initial cards on the table: ${Decks.TABLE.deck.joinToString(" ")}")
}

fun exit() {
    println("Game Over")
}


