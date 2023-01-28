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
    USER(mutableListOf()),
    COMPUTER(mutableListOf())
}

enum class Turn(var turn: Boolean) {
    USER(false),
    COMPUTER(false)
}

class Card(val rank: Ranks, val suit: Suits) {
    override fun toString() = "${rank.symbol}${suit.symbol}"
}


fun main() {
    start()
    game()
}

private fun game() {
    while (Decks.DECK.deck.size > 0) {
        println("${Decks.TABLE.deck.size} cards on the table, and the top card is ${Decks.TABLE.deck.first()}")

        if (Turn.USER.turn) {
            putCard(Decks.USER)
        } else if ((Turn.COMPUTER.turn)) {
            putCard(Decks.COMPUTER)
        }
        printAllDecks()
        Decks.DECK.deck.clear()
    }
}

private fun start() {
    println("Indigo Card Game\nPlay first?")
    firstTurn()
//    whoseTurn() // This test!

//    Let's get(create) a deck of cards
    createDeck()

//    Deal cards on table
    initialTable()

//    Hand out cards to players
    dealCards()
//    printAllDecks() // This test!
}

private fun firstTurn() {
    when (readln()) {
        "yes" -> Turn.USER.turn = true
        "no" -> Turn.COMPUTER.turn = true
        else -> {
            println("Incorrect choice, please enter \"yes\" or \"no\"")
            firstTurn()
        }
    }
}

private fun createDeck() {
    Suits.values().forEach { suit ->
        Ranks.values().forEach { rank ->
            Decks.DECK.deck.add(Card(rank, suit))
        }
    }
    Decks.DECK.deck.shuffle()
}

private fun initialTable() {
    Decks.TABLE.deck = Decks.DECK.deck.subList(0, 4).asReversed()
    println("Initial cards on the table: ${Decks.TABLE.deck.joinToString(" ")}")

    // crutch for deleting a value  -  fix this!
    val temp = Decks.DECK.deck.filter { it !in Decks.TABLE.deck }.toMutableList()
    Decks.DECK.deck = temp
}

private fun dealCards() {
    Decks.DECK.deck.subList(0, 13).forEachIndexed { index, card ->
        if (index % 2 == 0) {
            Decks.COMPUTER.deck.add(card)
        } else {
            Decks.USER.deck.add(card)
        }
    }
    Decks.DECK.deck.removeAll(Decks.COMPUTER.deck)
    Decks.DECK.deck.removeAll(Decks.USER.deck)
}

private fun cardsInHand() {
    var number = 0
    print("Cards in hand: ")
    Decks.USER.deck.forEach {
        number += 1
        print("$number)$it ")
    }
    println("")
}

private fun putCard(player: Decks) {
    if (player == Decks.USER) {
        cardsInHand()
        println("\nChoose a card to play (1-${Decks.USER.deck.size}):")

        // fix out of range
        val card = Decks.USER.deck[readln().toInt() - 1]
        Decks.TABLE.deck.add(0, card)
        player.deck.remove(card)
    } else if (player == Decks.COMPUTER) {

    }

}

fun exit() {
    println("Game Over")
}


// Tests __________________________________________________________
private fun whoseTurn() {
    Turn.values().forEach { println("${it.name} - ${it.turn} ") }
}

private fun printAllDecks() {
    println(
        """
        deck -> ${Decks.DECK.deck.joinToString(" ")}
        table -> ${Decks.TABLE.deck.joinToString(" ")}
        user -> ${Decks.USER.deck.joinToString(" ")}
        computer -> ${Decks.COMPUTER.deck.joinToString(" ")}
        """.trimIndent()
    )
}


