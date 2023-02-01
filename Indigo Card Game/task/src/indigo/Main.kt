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

class Card(val rank: Ranks, val suit: Suits) {
    override fun toString() = "${rank.symbol}${suit.symbol}"
}

enum class Decks(var deck: MutableList<Card>) {
    DECK(mutableListOf()),
    TABLE(mutableListOf()),
    USER(mutableListOf()),
    COMPUTER(mutableListOf())
}

enum class Players(var turn: Boolean, var win: Boolean) {
    USER(false, false),
    COMPUTER(false, false)
}

fun main() {
    // Start the game
    startGame()

    // Main game
    game()
}

private fun startGame() {
    println("Indigo Card Game")

//    Ask questions until we find out who goes first
    while (!Players.USER.turn && !Players.COMPUTER.turn) {
        println("Play first?")
        when (readln().lowercase()) {
            "yes" -> Players.USER.turn = true
            "no" -> Players.COMPUTER.turn = true
        }
    }

//    Let's get(create) a deck and deal cards on table
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

//    Hand out cards to players
    dealCards()
}

private fun game() {
    fun messageText() =
        println("\n${Decks.TABLE.deck.size} cards on the table, and the top card is ${Decks.TABLE.deck.last()}")

//    Game continues while players have a turn
    while (Players.USER.turn || Players.COMPUTER.turn) {
        messageText()

        if (Decks.USER.deck.isEmpty() && Decks.COMPUTER.deck.isEmpty()) dealCards()

        when {
            Players.USER.turn -> {
                val cards = Decks.USER.deck.mapIndexed { index, card -> "${index + 1})$card" }
                    .joinToString(" ")
                println("Cards in hand: $cards")
                putCard(Players.USER)
            }
            Players.COMPUTER.turn -> putCard(Players.COMPUTER)
        }

        if (
            Decks.DECK.deck.isEmpty() &&
            Decks.TABLE.deck.size == 52 &&
            Decks.USER.deck.isEmpty() &&
            Decks.COMPUTER.deck.isEmpty()
        ) {
            messageText()
            gameOver()
        }
    }
}

fun showStatistic(card: Card) {
    val scores = mutableMapOf(Players.USER to 0, Players.COMPUTER to 0)
    val cards = mutableMapOf(Players.USER to 0, Players.COMPUTER to 0)

    val player = if (card == Decks.DECK.deck.last()) Players.USER else Players.COMPUTER

    scores[player]?.plus(1)


    println(
        """
        Score: Player ${scores[Players.USER]} - Computer ${scores[Players.COMPUTER]}
        Cards: Player ${cards[Players.USER]} - Computer ${cards[Players.COMPUTER]}""".trimIndent()
    )
}

private fun dealCards() {
    // Take turns dealing out cards
    for (index in 0..11) {
        if (index % 2 == 0) {
            Decks.COMPUTER.deck.add(Decks.DECK.deck[index])
        } else {
            Decks.USER.deck.add(Decks.DECK.deck[index])
        }
    }

    // Remove cards from deck
    Decks.DECK.deck.removeAll(Decks.COMPUTER.deck)
    Decks.DECK.deck.removeAll(Decks.USER.deck)
}

private fun putCard(player: Players) {
    val card: Card
    val sizeDeck = Decks.USER.deck.size

    fun put(decks: Decks, card: Card) {
        showStatistic(card)
        // Put card on table and remove from deck
        Decks.TABLE.deck.add(Decks.TABLE.deck.size, card)
        decks.deck.remove(card)

        // Change players turn
        Players.USER.turn = true
        Players.COMPUTER.turn = true
        player.turn = false
    }

    when (player) {
        // when user move
        Players.USER -> {
            // output cards in hand user
            println("Choose a card to play (1-$sizeDeck):")

            // received player choice
            when (val choice = readln()) {
                in (1..sizeDeck).toString() -> {
                    card = Decks.USER.deck[choice.toInt() - 1]
                    put(Decks.USER, card)
                }
                "exit" -> {
                    gameOver()
                }
                else -> putCard(player)
            }
        }
        // when computer move
        Players.COMPUTER -> {
            card = Decks.COMPUTER.deck.first()
            println("Computer plays $card")
            put(Decks.COMPUTER, card)
        }
    }


}

fun gameOver() {
//    Turn off options to move for player and computer
    Players.USER.turn = false
    Players.COMPUTER.turn = false

    println("Game Over")
}


