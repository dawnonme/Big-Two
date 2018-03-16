import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JOptionPane;


/**
 * This class is designed to model a Big Two game. In this class, whole game logic is implemented. And the client for one player
 * is modeled to connect to the Big Two server.
 * 
 * @author Li Gengyu
 *
 */
public class BigTwoClient implements CardGame, NetworkGame {
	
	private int numOfPlayers; // An integer specifying the number of players.
	private Deck deck; // A deck of cards.
	private ArrayList<CardGamePlayer> playerList; // A list of players.
	private ArrayList<Hand> handsOnTable; // A list of hands played on the table.
	private int playerID; // An integer specifying the playerID (i.e., index) of the local player.
	private String playerName; // A string specifying the name of the local player.
	private String serverIP; // A string specifying the IP address of the game server.
	private int serverPort; // An integer specifying the TCP port of the game server.
	private Socket sock; // A socket connection to the game server.
	private ObjectOutputStream oos; // An ObjectOutputStream for sending messages to the server.
	private int currentIdx; // An integer specifying the index of the player for the current turn.
	private BigTwoTable table; // A Big Two table which builds the GUI for the game and handles all user actions
	private boolean firstMove; // A boolean indicates if this is the first move
	
	/** 
	 * This is the constructor of BigTwo, it create an object of playerList and handsOnTable and bigTwoTable,
	 * this constructor also create four players (CardGameplayer object) to model the four players in this game.
	 * Then these four players are added into playerList.
	 * 
	 */
	public BigTwoClient() {
		playerList = new ArrayList<CardGamePlayer>();
		handsOnTable = new ArrayList<Hand>();
		for (int i = 0; i < 4; i++) {
			CardGamePlayer player = new CardGamePlayer();
			player.setName("");
			playerList.add(player);
		}
		table = new BigTwoTable(this);
		makeConnection();
	}
	
	/**
	 * Getter of the class to retrieve the deck in game (deck).
	 * 
	 * @return
	 * 			Return a Deck object which is the card deck used in the game.
	 */
	@Override
	public Deck getDeck() {
		return deck;
	}
	
	/**
	 * Getter of the class to retrieve the player list in game (playerList).
	 * 
	 * @return
	 * 			Return a CardGamePlayer ArrayList which store the 4 players in the game.
	 */
	@Override
	public ArrayList<CardGamePlayer> getPlayerList() {
		return playerList;
	}
	
	/**
	 * Getter of the class to retrieve the played hands on table (handsOnTable).
	 * 
	 * @return
	 * 			Return a Hand ArrayList which store all the hands that have been played on table.
	 */
	@Override
	public ArrayList<Hand> getHandsOnTable() {
		return handsOnTable;
	}
	
	/**
	 * Getter of the class to retrieve the current index of player, i.e. which player is playing now (currentInx)
	 * 
	 * @return
	 * 			Return an integer to show the current index of player
	 */
	@Override
	public int getCurrentIdx() {
		return currentIdx;
	}
	
	/**
	 * This method can start a new game. This method will clear all the cards in players' hands and uses 
	 * the shuffled new deck to distribute cards to the players. Then sets the current player, initializes
	 * the game table.
	 * @param deck
	 * 				The deck which has been shuffled and used in the game, from which we distribute cards to players.
	 */
	@Override
	public void start(Deck deck) {
		for (int i = 0; i < 4; i++) {
			playerList.get(i).getCardsInHand().removeAllCards();
		}
		table.reset();
		int startingPos = 0;
		for (int i = 0; i < 4; i++) {
			for (int j = startingPos; j < startingPos + 13; j++) {
				playerList.get(i).addCard(deck.getCard(j));
			}
			playerList.get(i).sortCardsInHand();
			startingPos += 13;
			if (playerList.get(i).getCardsInHand().contains(new Card(0, 2))) {
				currentIdx = i;
			}
		}
		
		table.setActivePlayer(currentIdx);
		firstMove = true;
		table.repaint();
		table.printMsg("Enjoy the big two game!!!\n");
		table.printMsg("The star specifies the current player to make the move.\n");
		table.printMsg(playerList.get(currentIdx).getName() + " starting!\n");
	}
	
	/**
	 * To judge whether the game is over or not, when one player run out of cards, game is over, the currentIdx
	 * player wins the game.
	 * 
	 * @return
	 * 			Return a boolean value to show whether the game is over or not, true if over and vice versa.
	 */
	@Override
	public boolean endOfGame() {
		for (int i = 0; i < playerList.size(); i++) {
			if (playerList.get(i).getNumOfCards() == 0) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Method to judge whether the input indices of cards in hand which will be used to compose a hand is valid.
	 * If the input indices are out of bound, return true and vice versa, this method is designed to handle the
	 * invalid input case. If the indices are invalid, ask the player to input again. 
	 * 
	 * @param cardIdx
	 * 						An integer array stores the input indices of the cards.
	 * 
	 * @return
	 * 			A boolean value indicates whether the indices are out of bound or not.
	 */
	public boolean outOfBound(int[] cardIdx) {
		if (cardIdx == null) {
			return false;
		}
		for (int i = 0; i < cardIdx.length; i++) {
			if (cardIdx[i] < 0 || cardIdx[i] >= playerList.get(currentIdx).getCardsInHand().size()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Method to print a hand onto the msgArea, if no, print {pass}, else print the hand with the
	 * hand type and list of card including suits and ranks.
	 * 
	 * @param cards
	 * 				The printed hand
	 */
	public void printToTable(Hand cards) {
		if (cards != null) {
			table.printMsg("{" + cards.getType() + "} ");
			for (int i = 0; i < cards.size(); i++) {
				String string = "[" + cards.getCard(i) + "] ";
				table.printMsg(string);
			}
		} else {
			table.printMsg("{pass}\n");
		}
	}
	
	/**
	 * Method to get the number of players.
	 * 
	 * @return
	 * 			Return number of players.
	 * 
	 * @see CardGame#getNumOfPlayers()
	 */
	@Override
	public int getNumOfPlayers() {
		return numOfPlayers;
	}

	/**
	 * Method to check move by a given player with a given cards selected list. If the move is illegal,
	 * print error message to the msgArea or else make the move. If it is end of game after a move, 
	 * this method is also in charge of that. This method will do according to different card combinations.
	 * 
	 * @param playerID
	 * 					Player ID who is about to make move.
	 * 
	 * @param cardIdx
	 * 					An array of indices indicating the list of cards that the player is about to make.
	 * 
	 * @see CardGame#checkMove(int, int[])
	 */
	@Override
	public void checkMove(int playerID, int[] cardIdx) {		
		table.printMsg(playerList.get(playerID).getName() + "'s turn:\n");
		if (firstMove) {
			if (cardIdx == null || (cardIdx != null && composeHand(playerList.get(currentIdx), playerList.get(currentIdx).play(cardIdx)) == null) || outOfBound(cardIdx)) {
				table.printMsg("Not a legal move! Try again!\n");
			} else if (composeHand(playerList.get(currentIdx), playerList.get(currentIdx).play(cardIdx)) != null) {
				printToTable(composeHand(playerList.get(currentIdx), playerList.get(currentIdx).play(cardIdx)));
				table.printMsg("\n");
				handsOnTable.add(composeHand(playerList.get(currentIdx), playerList.get(currentIdx).play(cardIdx)));
				firstMove = false;
				Arrays.sort(cardIdx);
				for (int i = cardIdx.length - 1; i >= 0; i--) {
					playerList.get(currentIdx).getCardsInHand().removeCard(cardIdx[i]);
				}
				currentIdx++;
				currentIdx %= 4;
				table.setActivePlayer(currentIdx);
				table.resetSelected();
				table.repaint();
			}
		} else {
			if (handsOnTable.get(handsOnTable.size() - 1).getPlayer().equals(playerList.get(currentIdx))) {
				if (cardIdx == null || (cardIdx != null && composeHand(playerList.get(currentIdx), playerList.get(currentIdx).play(cardIdx)) == null) || outOfBound(cardIdx)) {
					table.printMsg("Not a legal move! Try again!\n");
				} else if (composeHand(playerList.get(currentIdx), playerList.get(currentIdx).play(cardIdx)) != null) {
					printToTable(composeHand(playerList.get(currentIdx), playerList.get(currentIdx).play(cardIdx)));
					table.printMsg("\n");
					handsOnTable.add(composeHand(playerList.get(currentIdx), playerList.get(currentIdx).play(cardIdx)));
					Arrays.sort(cardIdx);
					for (int i = cardIdx.length - 1; i >= 0; i--) {
						playerList.get(currentIdx).getCardsInHand().removeCard(cardIdx[i]);
					}
					currentIdx++;
					currentIdx %= 4;
					table.setActivePlayer(currentIdx);
					table.resetSelected();
					table.repaint();
				}
			} else {
				if (cardIdx != null && composeHand(playerList.get(currentIdx), playerList.get(currentIdx).play(cardIdx)) == null || outOfBound(cardIdx)) {
					table.printMsg("Not a legal move! Try again!\n");
				} else if (cardIdx == null) {
					printToTable(composeHand(playerList.get(currentIdx), playerList.get(currentIdx).play(cardIdx)));
					currentIdx++;
					currentIdx %= 4;
					table.setActivePlayer(currentIdx);
					table.resetSelected();
					table.repaint();
					
				} else if (!composeHand(playerList.get(currentIdx), playerList.get(currentIdx).play(cardIdx)).beats(handsOnTable.get(handsOnTable.size() - 1))) {
					printToTable(composeHand(playerList.get(currentIdx), playerList.get(currentIdx).play(cardIdx)));
					table.printMsg("<=== Not a legal move! Try again!\n");
				} else if (composeHand(playerList.get(currentIdx), playerList.get(currentIdx).play(cardIdx)).beats(handsOnTable.get(handsOnTable.size() - 1))) {
					printToTable(composeHand(playerList.get(currentIdx), playerList.get(currentIdx).play(cardIdx)));
					table.printMsg("\n");
					handsOnTable.add(composeHand(playerList.get(currentIdx), playerList.get(currentIdx).play(cardIdx)));
					Arrays.sort(cardIdx);
					for (int i = cardIdx.length - 1; i >= 0; i--) {
						playerList.get(currentIdx).getCardsInHand().removeCard(cardIdx[i]);
					}
					currentIdx++;
					currentIdx %= 4;
					table.setActivePlayer(currentIdx);
					table.resetSelected();
					table.repaint();
				}
			}
		}
		if (endOfGame()) {
			
			String info = "Game ends!\n";
			int winner = -1;
			for (int i = 0; i < 4 && winner == -1; i++) {
				if (playerList.get(i).getNumOfCards() == 0) {
					winner = i;
				}
			}
			for (int i = 0; i < 4; i++) {
				info += playerList.get(i).getName();
				if (i == winner) {
					info += " wins the game!\n";
				} else {
					info += (" has " + playerList.get(i).getNumOfCards() + " cards in hand.\n");
				}
			}
			JOptionPane.showMessageDialog(null, info);
			table.disable();
			sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));
		}
	}

	/**
	 * This method models one player making move, it simply send a message MSG to the server.
	 * 
	 * @param playerID
	 * 					Player ID who is about to make move.
	 * 
	 * @param cardIdx
	 * 					An array of indices indicating the list of cards that the player is about to make.
	 * 
	 * @see CardGame#makeMove(int, int[])
	 */
	@Override
	public void makeMove(int playerID, int[] cardIdx) {
		sendMessage(new CardGameMessage(CardGameMessage.MOVE, -1, cardIdx));
	}

	/**
	 * This is the main method of the whole project, which create a BigTwoClient object to start the
	 * client.
	 * @param args
	 * 				Not used.
	 */
	public static void main(String[] args) {
		BigTwoClient game = new BigTwoClient();	
	}
	
	/**
	 * This method take a CardList and a CardGamePlayer objects as parameters, the method checks whether the
	 * list of cards can be composed as a valid hand which is implemented in other subclasses of Hand class,
	 * if no valid hand can be composed, this method return null to indicate that. This method is used to handle
	 * the invalid input by players.
	 * 
	 * @param player
	 * 				The player who plays this turn.
	 * 
	 * @param cards
	 * 				The list of card that the player choose to play.
	 * 
	 * @return
	 * 			If the combination of card can be a valid hand among Single, Pair, Triple, StraightFlush, Quad,
	 * 			FullHouse, Flush or Straight, just return this hand object, if not, return null.
	 */
	public static Hand composeHand(CardGamePlayer player, CardList cards) {
		if (cards == null || player == null) {
			return null;		
		}
		if (cards.size() == 1) {
			return new Single(player, cards);
		} else if (cards.size() == 2) {
			Hand hand = new Pair(player, cards);
			if (hand.isValid()) {
				return hand;
			} else {
				return null;
			}
		} else if (cards.size() == 3) {
			Hand hand = new Triple(player, cards);
			if (hand.isValid()) {
				return hand;
			} else {
				return null;
			}
		} else if (cards.size() == 5) {
			Hand hand = new StraightFlush(player, cards);
			if (hand.isValid()) {
				return hand;
			}
			hand = new Quad(player, cards);
			if (hand.isValid()) {
				return hand;
			}
			hand = new FullHouse(player, cards);
			if (hand.isValid()) {
				return hand;
			}
			hand = new Flush(player, cards);
			if (hand.isValid()) {
				return hand;
			}
			hand = new Straight(player, cards);
			if (hand.isValid()) {
				return hand;
			}
		}
		return null;
	}

	/**
	 * Getter for retrieving the playerID for the client.
	 * @return Return the playerID for this client
	 * @see NetworkGame#getPlayerID()
	 */
	@Override
	public int getPlayerID() {
		return playerID;
	}

	/**
	 * Setter for set the playerID for the client.
	 * @param playerID
	 * 					A playerID for setting.
	 * @see NetworkGame#setPlayerID(int)
	 */
	@Override
	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}

	/**
	 * Getter for retrieving the player name entered by user of this client.
	 * @return Return the player name.
	 * @see NetworkGame#getPlayerName()
	 */
	@Override
	public String getPlayerName() {
		return playerName;
	}

	/**
	 * Setter for setting the playerName of this client.
	 * @param playerName
	 * 					A play name string for setting.
	 * @see NetworkGame#setPlayerName(java.lang.String)
	 */
	@Override
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	/**
	 * Getter for retrieving the serverIP.
	 * @return Return the serverIP.
	 * @see NetworkGame#getServerIP()
	 */
	@Override
	public String getServerIP() {
		return serverIP;
	}

	/**
	 * Setter for setting the serverIP.
	 * @param serverIP
	 * 					The serverIP for connection.
	 * @see NetworkGame#setServerIP(java.lang.String)
	 */
	@Override
	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}

	/**
	 * Getter for retrieving the serverPort.
	 * @return Return the serverPort.
	 * @see NetworkGame#getServerPort()
	 */
	@Override
	public int getServerPort() {
		return serverPort;
	}

	/**
	 * Setter for setting the serverPort.
	 * @param serverPort
	 * 					The serverPort for connection.
	 * @see NetworkGame#setServerPort(int)
	 */
	@Override
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}
	

	/**
	 * This method will be called automatically when a client object is created or the connnect item of 
	 * the menu is clicked. This method can make a connection with the game server.
	 * @see NetworkGame#makeConnection()
	 */
	@Override
	public synchronized void makeConnection() {
		playerName = JOptionPane.showInputDialog("Please enter you name: ");
		if (playerName != null) {
			try {
				sock = new Socket("127.0.0.1", 2396);
				serverIP = "127.0.0.1";
				serverPort = 2396;
				oos = new ObjectOutputStream(sock.getOutputStream());
				Thread receiveMessage = new Thread(new ServerHandler());
		 		receiveMessage.start();
				sendMessage(new CardGameMessage(CardGameMessage.JOIN, -1, playerName));
				sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * This method handles all the incoming message sent by the server and do according to different 
	 * message.
	 * @param message
	 * 					The CardGameMessage the server sent to the client.
	 * @see NetworkGame#parseMessage(GameMessage)
	 */
	@Override
	public synchronized void parseMessage(GameMessage message) {
		if (message.getType() == CardGameMessage.PLAYER_LIST) {
			playerID = message.getPlayerID();
			for (int i = 0; i < playerList.size(); i++) {
				if (((String[]) message.getData())[i] != null) {
					playerList.get(i).setName(((String[]) message.getData())[i]);
				}
			}
		} else if (message.getType() == CardGameMessage.FULL) {
			table.printMsg("The server is now full, can't join the game! Wait a minute and try again.\n");
		} else if (message.getType() == CardGameMessage.QUIT) {
			playerList.get(message.getPlayerID()).setName("");
			stopTheGame();
			sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));
		} else if (message.getType() == CardGameMessage.JOIN) {
			playerList.get(message.getPlayerID()).setName((String) message.getData());
		} else if (message.getType() == CardGameMessage.READY) {
			int id = message.getPlayerID();
			table.printMsg(playerList.get(id).getName() + " is ready!\n");
			table.disableTheConnectItem();
		} else if (message.getType() == CardGameMessage.START) {
			table.enable();
			start((BigTwoDeck) message.getData());
		} else if (message.getType() == CardGameMessage.MSG) {
			table.printChatMsg((String) message.getData() + "\n");
		} else if (message.getType() == CardGameMessage.MOVE) {
			checkMove(message.getPlayerID(), (int[]) message.getData());
		}
	}

	/**
	 * The method can send message to the server for the clients.
	 * @param message
	 * 					The CardGameMessage that the client is about to send to the server.
	 * @see NetworkGame#sendMessage(GameMessage)
	 */
	@Override
	public synchronized void sendMessage(GameMessage message) {
		try {
			oos.writeObject(message);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * This method can stop the game when the game is in progress, that is, when one of the player
	 * quits during the game. 
	 */
	public void stopTheGame() {
		int checkPlayerSum = 0;
		for (int i = 0; i < 4; i++) {
			if (playerList.get(i).getName() == "") {
				checkPlayerSum++;
			}
		}
		if (checkPlayerSum == 1) {
			for (int i = 0; i < 4; i++) {
				playerList.get(i).getCardsInHand().removeAllCards();
			}
			handsOnTable = new ArrayList<Hand>();
			table.reset();
			table.clearChatMsg();
			table.repaint();
			table.disable();
		}
	}
	
	/**
	 * This class is designed for creating a new thread for receiving the message from the server.
	 * @author Li Gengyu
	 *
	 */
	class ServerHandler implements Runnable {
		
		/**
		 * Method which will be called when the thread starts. When there is message from the server,
		 * read it in.
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			try {
				ObjectInputStream os = new ObjectInputStream(sock.getInputStream());
				CardGameMessage message = (CardGameMessage) os.readObject();
				while (message != null) {
					parseMessage(message);
					table.repaint();
					message = (CardGameMessage) os.readObject();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
