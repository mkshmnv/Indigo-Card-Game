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

class Card(val rank: Ranks, val suit: Suits) {
    override fun toString() = "${rank.symbol}${suit.symbol}"
}
enum class Move(var turn: Boolean) {
    USER(false),
    COMPUTER(false)
}


fun main() {
    start()
    game()
}





private fun start() {
    println("Indigo Card Game")

    fun firstTurn() {
        println("Play first?")
        when (readln().lowercase()) {
            "yes" -> Move.USER.turn = true
            "no" -> Move.COMPUTER.turn = true
            else -> firstTurn()
        }
    }

    fun initialTable() {
        Suits.values().forEach { suit ->
            Ranks.values().forEach { rank ->
                Decks.DECK.deck.add(Card(rank, suit))
            }
        }
        Decks.DECK.deck.shuffle()

        Decks.TABLE.deck = Decks.DECK.deck.subList(0, 4)
        println("Initial cards on the table: ${Decks.TABLE.deck.joinToString(" ")}")

        // TODO() FIX crutch for deleting a value  -  fix this!
        val temp = Decks.DECK.deck.filter { it !in Decks.TABLE.deck }.toMutableList()
        Decks.DECK.deck = temp
    }

//    Whois first
    firstTurn()

//    Let's get(create) a deck and deal cards on table
    initialTable()

//    Hand out cards to players
    dealCards()
//    printAllDecks() // This test!
}

private fun game() {
    var continueGame = true
    val table = Decks.TABLE.deck
    while (continueGame) {
        println("\n${table.size} cards on the table, and the top card is ${table.last()}")

        if (Decks.TABLE.deck.size == 52) {
            continueGame = exit()
            break
        }

        if (Decks.USER.deck.isEmpty() && Decks.COMPUTER.deck.isEmpty() && Decks.DECK.deck.isNotEmpty()) dealCards()

        if (Move.USER.turn) {
            cardsInHand()
            putCard(Move.USER)
        } else {
            putCard(Move.COMPUTER)
        }
//        printAllDecks()
    }
    println("\n${table.size} cards on the table, and the top card is ${table.last()}")
    exit()
}



private fun dealCards() {
    for (index in 0..11) {
        if (index % 2 == 0) {
            Decks.COMPUTER.deck.add(Decks.DECK.deck[index])
        } else {
            Decks.USER.deck.add(Decks.DECK.deck[index])
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

private fun putCard(player: Move): Boolean {
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
            println("\nChoose a card to play (1-$sizeDeck):")

            // process received choice
            when (val choice = readln()) {
                in List(sizeDeck) { (it + 1).toString() } -> {
                    card = Decks.USER.deck[choice.toInt() - 1]
                    Decks.TABLE.deck.add(Decks.TABLE.deck.size, card)
                    Decks.USER.deck.remove(card)
                    switchPlayer()
                }
                "exit" -> return exit()
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
    return true
}


fun exit(): Boolean {
    println("Game Over")
    return false
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


