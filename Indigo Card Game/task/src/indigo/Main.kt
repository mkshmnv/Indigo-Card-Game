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

enum class Deck(var cards: MutableList<Card>) {
    GAME_DECK(mutableListOf()),
    TABLE_DECK(mutableListOf())
}

enum class Players(var value: Player) {
    PLAYER(Player("Player")),
    COMPUTER(Player("Computer"))
}

class Player(val name: String) {
    var firstTurn: Boolean = false
    var turn: Boolean = false
    var deck: MutableList<Card> = mutableListOf()
    var score: Int = 0
    var winsCards: MutableList<Card> = mutableListOf()

    fun getStatistic() {
        println("Score: Player ${Players.PLAYER.value.score} - Computer ${Players.COMPUTER.value.score}")
        println("Cards: Player ${Players.PLAYER.value.winsCards.size} - Computer ${Players.COMPUTER.value.winsCards.size}")
    }
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
    while (!Players.PLAYER.value.turn && !Players.COMPUTER.value.turn) {
        println("Play first?")
        when (readln().lowercase()) {
            "yes" -> {
                Players.PLAYER.value.firstTurn = true
                Players.PLAYER.value.turn = true
            }
            "no" -> {
                Players.COMPUTER.value.firstTurn = true
                Players.COMPUTER.value.turn = true
            }
        }
    }

//    Let's get(create) a deck and deal cards on table
    Suits.values().forEach { suit ->
        Ranks.values().forEach { rank ->
            Deck.GAME_DECK.cards.add(Card(rank, suit))
        }
    }
    Deck.GAME_DECK.cards.shuffle()

    Deck.TABLE_DECK.cards = Deck.GAME_DECK.cards.subList(0, 4)
    println("Initial cards on the table: ${Deck.TABLE_DECK.cards.joinToString(" ")}")

    // TODO() FIX crutch for deleting a value  -  fix this!
    val temp = Deck.GAME_DECK.cards.filter { it !in Deck.TABLE_DECK.cards }.toMutableList()
    Deck.GAME_DECK.cards = temp

//    Hand out cards to players
    dealCards()
}

private fun game() {
    fun messageText() = if (Deck.TABLE_DECK.cards.isEmpty())
        println("No cards on the table")
    else
        println("\n${Deck.TABLE_DECK.cards.size} cards on the table, and the top card is ${Deck.TABLE_DECK.cards.last()}")

//    Game continues while players have a turn
    while (Players.PLAYER.value.turn || Players.COMPUTER.value.turn) {
        messageText()

        if (Players.PLAYER.value.deck.isEmpty() && Players.COMPUTER.value.deck.isEmpty()) dealCards()

        when {
            Players.PLAYER.value.turn -> {
                val cards = Players.PLAYER.value.deck.mapIndexed { index, card -> "${index + 1})$card" }.joinToString(" ")
                println("Cards in hand: $cards")
                move(Players.PLAYER)
            }
            Players.COMPUTER.value.turn -> move(Players.COMPUTER)
        }

        if (
            Deck.GAME_DECK.cards.isEmpty() &&
            Deck.TABLE_DECK.cards.size == 52 &&
            Players.PLAYER.value.deck.isEmpty() &&
            Players.COMPUTER.value.deck.isEmpty()
        ) {
            messageText()
            gameOver()
        }
    }
}



private fun dealCards() {
    // Take turns dealing out cards
    for (index in 0..11) {
        if (index % 2 == 0) {
            Players.COMPUTER.value.deck.add(Deck.GAME_DECK.cards[index])
        } else {
            Players.PLAYER.value.deck.add(Deck.GAME_DECK.cards[index])
        }
    }

    // Remove cards from deck
    Deck.GAME_DECK.cards.removeAll(Players.COMPUTER.value.deck)
    Deck.GAME_DECK.cards.removeAll(Players.PLAYER.value.deck)
}

private fun move(currentPlayer: Players) {
    val currentPlayerValues = currentPlayer.value
    val tableCards = Deck.TABLE_DECK.cards

    lateinit var playingCard: Card
    val cardOnTable = Deck.GAME_DECK.cards.last()

    fun statistic() {
        if (playingCard.rank == cardOnTable.rank || playingCard.suit == cardOnTable.suit) {
            println("${currentPlayerValues.name} wins cards")
            currentPlayerValues.score += 1
            currentPlayerValues.winsCards.addAll(tableCards)

            tableCards.clear()

            currentPlayerValues.getStatistic()
        }
    }

    fun putCard() {
        // Put card on table and remove card from deck
        statistic()

        tableCards.add(tableCards.size, playingCard)
        currentPlayerValues.deck.remove(playingCard)



        // Change players turn
        Players.PLAYER.value.turn = true
        Players.COMPUTER.value.turn = true
        currentPlayerValues.turn = false
    }

    when (currentPlayer) {
        // when user move
        Players.PLAYER -> {
            // output cards in hand user
            println("Choose a card to play (1-${currentPlayerValues.deck.size}):")

            // received player choice
            when (val choice = readln()) {
                in (1..currentPlayerValues.deck.size).map { it.toString() } -> {
                    playingCard = currentPlayerValues.deck[choice.toInt() - 1]
                    putCard()
                }
                "exit" -> {
                    gameOver()
                }
                else -> move(currentPlayer)
            }
        }
        // when computer move
        Players.COMPUTER -> {
            playingCard = currentPlayerValues.deck.first()
            println("Computer plays $playingCard")
            putCard()
        }
    }
}

fun gameOver() {
//    Turn off options to move for player and computer
    Players.PLAYER.value.turn = false
    Players.COMPUTER.value.turn = false

    println("Game Over")
}


