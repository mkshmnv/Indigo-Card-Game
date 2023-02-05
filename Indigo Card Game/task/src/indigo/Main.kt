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

enum class Move(var turn: Boolean) {
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

private fun start() {
    println("Indigo Card Game\nPlay first?")
    firstTurn()
//    whoseTurn() // This test!

//    Deal cards on table
    initialTable()

//    Hand out cards to players
    dealCards()
//    printAllDecks() // This test!
}

private fun game() {

    while (Decks.TABLE.deck.size <= 52) {
        val table = Decks.TABLE.deck

        println("\n${table.size} cards on the table, and the top card is ${table.last()}")

        if (Decks.USER.deck.isEmpty() || Decks.COMPUTER.deck.isEmpty()) dealCards()

        if (Move.USER.turn) {
            cardsInHand()
            putCard(Move.USER)
        } else {
            putCard(Move.COMPUTER)
        }
        printAllDecks()
    }
    println("Game Over")
}

private fun firstTurn() {
    when (readln()) {
        "yes" -> Move.USER.turn = true
        "no" -> Move.COMPUTER.turn = true
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

    //    Let's get(create) a deck of cards
    createDeck()

    Decks.TABLE.deck = Decks.DECK.deck.subList(0, 4)
    println("Initial cards on the table: ${Decks.TABLE.deck.joinToString(" ")}")

    // crutch for deleting a value  -  fix this!
    val temp = Decks.DECK.deck.filter { it !in Decks.TABLE.deck }.toMutableList()
    Decks.DECK.deck = temp
}

private fun dealCards() {
    //TODO() FIX OUT OF RANGE
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
}

private fun putCard(player: Move) {
    val card: Card
    val sizeDeck = Decks.USER.deck.size

    fun switchPlayer() {
        Move.USER.turn = true
        Move.COMPUTER.turn = true
        player.turn = false
    }

    when (player) {
        // process when user move
        Move.USER -> {
            // output card in hand user
            // TODO() FIX postponement to next line
            println("Choose a card to play (1-$sizeDeck):")

            // process received choice
            when (val choice = readln()) {
                in List(sizeDeck) { (it + 1).toString() } -> {
                    card = Decks.USER.deck[choice.toInt() - 1]
                    Decks.TABLE.deck.add(Decks.TABLE.deck.size, card)
                    Decks.USER.deck.remove(card)
                    switchPlayer()
                }
                "exit" -> exit()
                else -> {
                    putCard(player)
                }
            }
        }
        // process when computer move
        Move.COMPUTER -> {
            card = Decks.COMPUTER.deck.first()
            println("Computer plays $card")
            Decks.TABLE.deck.add(Decks.TABLE.deck.size, card)
            Decks.COMPUTER.deck.remove(card)
            switchPlayer()
        }
    }
}


fun exit() {
    println("Game Over")
}


// Tests __________________________________________________________
private fun whoseTurn() {
    Move.values().forEach { println("${it.name} - ${it.turn} ") }
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


