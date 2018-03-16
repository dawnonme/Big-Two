import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * This class is designed to model the GUI of the big two game, it has a menu bar, a panel to display
 * the cards and players, two buttons for play and pass, and a text area to display the game information.
 * It implement CardGameTable interface.
 * 
 * @author Li Gengyu
 *
 */
public class BigTwoTable implements CardGameTable {

	private CardGame game; // A card game associates with this table
	private boolean[] selected; // A boolean array indicating which cards are being selected
	private int activePlayer; // An integer specifying the index of the active player
	private JFrame frame; // The main window of the application
	private JPanel bigTwoPanel; // A panel for showing the cards of each player and the cards played on the table
	private JButton playButton; // A "Play" button for the active player to play the selected cards
	private JButton passButton; // A "Pass" button for the active player to pass his/her turn to the next player
	private JTextArea msgArea; // A text area for showing the current game status as well as end of game messages
	private JTextField chatInput; // A text field for chat users' input
	private JTextArea chatContent; // A text area for displaying chat content
	private JMenuBar mb; // A menu bar for quit and connect
	private JMenu menu; // A menu to contain two items
	private JMenuItem qM; // The quit menu item
	private JMenuItem cM; // The connect menu item
	private Image[][] cardImages; // A 2D array storing the images for the faces of the cards
	private Image cardBackImage; // An image for the backs of the cards
	private Image[] avatars; // An array storing the images for the avatars
	private Image background; // An image for the background
	private Image star; // A star image specifying which is the current player to make move
	
	/**
	 * Constructor of the BigTwoTable class, it take a CardGame as parameter and make objects of all the 
	 * instance variables. It make a new panel to hold two buttons, a menu bar to hold one menu and two menu
	 * items, which is quit and restart, a text area to display the game information, and a draw panel to 
	 * display the cards and players.
	 * 
	 * @param game
	 * 				The relative card game, which the table is modeled.
	 */
	public BigTwoTable(CardGame game) {
		this.game = game;
		selected = new boolean[13];
		
		frame = new JFrame("Big Two!!!");
		frame.setSize(1200, 800);
		frame.setMinimumSize(new Dimension(1200, 800));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		  
		cardBackImage = new ImageIcon("back.jpg").getImage();
		avatars = new Image[4];
		for (int i = 0; i < 4; i++) {
			avatars[i] = new ImageIcon(Integer.toString(i) + ".jpg").getImage();
		}
		  
		cardImages = new Image[13][4];
		String suitsArr[] = new String[]{ "d.jpg", "c.jpg", "h.jpg", "s.jpg" };
		for (int i = 0; i < 13; i++) {
			for (int j = 0; j < 4; j++) {
				cardImages[i][j] = new ImageIcon(Integer.toString(i + 1) + suitsArr[j]).getImage();
		    }
		}	
		
		bigTwoPanel = new BigTwoPanel();
		bigTwoPanel.addMouseListener(new BigTwoPanel());
		frame.add(bigTwoPanel, BorderLayout.CENTER);
		
		background = new ImageIcon("background.jpg").getImage();
		  
		playButton = new JButton("play");
		playButton.setFont(new Font("Verdana", 1, 13));
		playButton.addActionListener(new PlayButtonListener());
		  
		passButton = new JButton("pass");
		passButton.setFont(new Font("Verdana", 1, 13));
		passButton.addActionListener(new PassButtonListener());
		chatInput = new JTextField(40);
		chatInput.setFont(new Font("Verdana", 0, 13));
		chatInput.addActionListener(new ChatInputListener());
		JLabel msgLabal = new JLabel("Messages: ");
		msgLabal.setFont(new Font("Verdana", 1, 13));
		msgLabal.setHorizontalAlignment(0);
		  
		JPanel buttonPanel = new JPanel(new GridLayout(1, 4));
		buttonPanel.add(playButton);
		buttonPanel.add(passButton);
		buttonPanel.add(msgLabal);
		buttonPanel.add(chatInput);
		frame.add(buttonPanel, BorderLayout.SOUTH);
		  
		mb = new JMenuBar();
		menu = new JMenu("Options");
		menu.setFont(new Font("Verdana", 1, 13));
		qM = new JMenuItem("Quit");
		qM.setFont(new Font("Verdana", 1, 13));
		qM.addActionListener(new QuitMenuItemListener());
		  
		cM = new JMenuItem("Connect");
		cM.addActionListener(new ConnectMenuItemListener());
		cM.setFont(new Font("Verdana", 1, 13));
		  
		menu.add(qM);
		menu.add(cM);
		mb.add(menu);
		frame.add(mb, BorderLayout.NORTH);
		  
		msgArea = new JTextArea(10, 40);
		msgArea.setEditable(false);
		msgArea.setFont(new Font("Courier New", 2, 14));
		  
		JScrollPane scroller = new JScrollPane(msgArea);
		msgArea.setLineWrap(true);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		chatContent = new JTextArea(10, 40);
		chatContent.setFont(new Font("Verdana", 0, 13));
		chatContent.setEditable(false);
		JScrollPane chatScroller = new JScrollPane(chatContent);
		chatContent.setLineWrap(true);
		chatScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		chatScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		JPanel textPanel = new JPanel();
		textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
		textPanel.add(scroller);
		textPanel.add(chatScroller);
		
		star = new ImageIcon("star.jpg").getImage();
		
		frame.add(textPanel, BorderLayout.EAST);
		frame.setVisible(true);
	}
	
	
	/**
	 * Sets the index of the active player (i.e., the current player).
	 * 
	 * @param activePlayer
	 *            an int value representing the index of the active player
	 * 
	 * @see CardGameTable#setActivePlayer(int)
	 */
	@Override
	public void setActivePlayer(int activePlayer) {
		this.activePlayer = activePlayer;
	}

	/**
	 * Returns an array of indices of the cards selected.
	 * 
	 * @return an array of indices of the cards selected
	 * 
	 * @see CardGameTable#getSelected()
	 */
	@Override
	public int[] getSelected() {
		int n = 0, index = 0;
		for (int i = 0; i < 13; i++) {
			if (selected[i]) {
				n++;
			}
		}
		if (n == 0) {
			return null;
		}
 		int[] indices = new int[n];
		for (int i = 0; i < 13; i++) {
			if (selected[i]) {
				indices[index] = i;
				index++;
			}
		}
		return indices;
	}

	/**
	 * Resets the list of selected cards to an empty list.
	 * 
	 * @see CardGameTable#resetSelected()
	 */
	@Override
	public void resetSelected() {
		for (int i = 0; i < selected.length; i++) {
			selected[i] = false;
		}
	}

	/**
	 * Repaints the GUI.
	 * 
	 * @see CardGameTable#repaint()
	 */
	@Override
	public void repaint() {
		frame.repaint();
		bigTwoPanel.repaint();		
	}

	/**
	 * Prints the specified string to the message area of the card game table.
	 * 
	 * @param msg
	 *            the string to be printed to the message area of the card game
	 *            table
	 *            
	 * @see CardGameTable#printMsg(java.lang.String)
	 */
	@Override
	public void printMsg(String msg) {
		msgArea.append(msg);
	}
	
	public void printChatMsg(String msg) {
		chatContent.append(msg);
	}

	/**
	 * Clears the message area of the card game table.
	 * 
	 * @see CardGameTable#clearMsgArea()
	 */
	@Override
	public void clearMsgArea() {
		msgArea.setText("");
	}
	
	/**
	 * Method for clearing the area for chat. 
	 */
	public void clearChatMsg() {
		chatContent.setText("");
	}
	
	/**
	 * Method for disabling the connect item when the client has been connected to the server.
	 */
	public void disableTheConnectItem() {
		cM.setEnabled(false);
	}

	/**
	 * Resets the GUI.oup
	 * 
	 * @see CardGameTable#reset()
	 */
	@Override
	public void reset() {
		resetSelected();
		clearMsgArea();
		enable();
	}

	/**
	 * Enables user interactions.
	 * 
	 * @see CardGameTable#enable()
	 */
	@Override
	public void enable() {
		playButton.setEnabled(true);
		passButton.setEnabled(true);
		bigTwoPanel.setEnabled(true);
	}

	/**
	 * Disables user interactions.
	 * 
	 * @see CardGameTable#disable()
	 */
	@Override
	public void disable() {
		playButton.setEnabled(false);
		passButton.setEnabled(false);
		bigTwoPanel.setEnabled(false);
		activePlayer = -1;
	}
	
	/**
	 * This class make a draw panel because it extends the JPanel and override the paintComponent
	 * method. And it is also in charge of the mouse click event of BigTwoPanel for it implements
	 * the MouseListener interface. 
	 * 
	 * @author Li Gengyu
	 *
	 */
	class BigTwoPanel extends JPanel implements MouseListener {
		
		/**
		 * A default serial version UID.
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * This method handles everything that should be painted on the panel, this method will be
		 * called automatically when repaint method is called to repaint everything every time 
		 * there is repaint. This  method will never be called explicitly. 
		 * 
		 * @param g
		 * 			A Graphics object for drawing the panel.
		 * 
		 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
		 */
		@Override
		public void paintComponent(Graphics g) {
			g.setColor(Color.WHITE);
			g.setFont(new Font("Verdana", 1, 15));
			g.drawImage(background, 0, 0, bigTwoPanel.getWidth(), bigTwoPanel.getHeight(), this);
			for (int i = 0; i < 4; i++) {
				String str = null;
				if (game.getPlayerList().get(i).getName() == "" || game.getPlayerList().get(i).getName() == null) {
					str = "";
				} else if (((BigTwoClient) game).getPlayerID() == i) {
					str = "You";
				} else {
					str = game.getPlayerList().get(i).getName();
				}
				g.drawString(str , 20, getHeight() * i / 5 + 20);
				if (i == 1 || i == 2 || i == 3) {
					g.drawLine(0, getHeight() * i / 5, 2000, getHeight() * i / 5);
				}
			}
			g.drawLine(0, getHeight() * 4 / 5, 2000, getHeight() * 4 / 5);
			String lastHand = "";
			if (game.getHandsOnTable().size() == 0) {
				lastHand = "";
			} else {
				lastHand += "Played by ";
				lastHand += game.getHandsOnTable().get(game.getHandsOnTable().size() - 1).getPlayer().getName();
			}
			g.drawString(lastHand, 20, getHeight() * 4 / 5 + 20);
			for (int i = 0; i < 4; i++) {
				if (game.getPlayerList().get(i).getName() != "") {
					g.drawImage(avatars[i], 10, getHeight() * i / 5 + 30, getHeight() / 7, getHeight() / 7, this);
					if (i == game.getCurrentIdx()) {
						g.drawImage(star, 80, getHeight() * i / 5 + 5, getHeight() / 27, getHeight() / 27, this);
					}
				}
			}
			for (int i = 0; i < 5; i++) {
				if (i == 4) {
					if (game.getHandsOnTable().size() > 0) {
						for (int j = 0; j < game.getHandsOnTable().get(game.getHandsOnTable().size() - 1).size(); j++) {
							g.drawImage(cardImages[game.getHandsOnTable().get(game.getHandsOnTable().size() - 1).getCard(j).getRank()][game.getHandsOnTable().get(game.getHandsOnTable().size() - 1).getCard(j).getSuit()], (j + 3) * getWidth() / 18, getHeight() * i / 5 + 30, getHeight() / 9, getHeight() / 7, this);
						}
					}	
				} else if (i == ((BigTwoClient) game).getPlayerID() || game.endOfGame()) {
					for (int j = 0; j < game.getPlayerList().get(i).getCardsInHand().size(); j++) {
						if (!selected[j]) {
							g.drawImage(cardImages[game.getPlayerList().get(i).getCardsInHand().getCard(j).getRank()][game.getPlayerList().get(i).getCardsInHand().getCard(j).getSuit()], (j + 3) * getWidth() / 18, getHeight() * i / 5 + 30, getHeight() / 9, getHeight() / 7, this);
						} else {
							g.drawImage(cardImages[game.getPlayerList().get(i).getCardsInHand().getCard(j).getRank()][game.getPlayerList().get(i).getCardsInHand().getCard(j).getSuit()], (j + 3) * getWidth() / 18, getHeight() * i / 5 + 10, getHeight() / 9, getHeight() / 7, this);
						}
					}
				} else {
					for (int j = 0; j < game.getPlayerList().get(i).getCardsInHand().size(); j++) {
						g.drawImage(cardBackImage, (j + 3) * getWidth() / 18, getHeight() * i / 5 + 30, getHeight() / 9, getHeight() / 7, this);
					}
				}
			}
		}

		/**
		 * This method handle the case when bigTwoPanel is clicked by mouse. It actually makes the clicked card
		 * selected or unselected.
		 * 
		 * @param e
		 * 			The mouse click event.
		 * 
		 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseClicked(MouseEvent e) {
			if (((BigTwoClient) game).getPlayerID() == game.getCurrentIdx()) {	
				if (checkMouseLocation(e.getX(), e.getY()) != -1) {
					selected[checkMouseLocation(e.getX(), e.getY())] = !selected[checkMouseLocation(e.getX(), e.getY())];	
				}
				BigTwoTable.this.repaint();
			}	
		}

		/**
		 * Not used.
		 * 
		 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
		 */
		@Override
		public void mousePressed(MouseEvent e) {}

		/**
		 * Not used.
		 * 
		 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseReleased(MouseEvent e) {}

		/**
		 * Not used.
		 * 
		 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseEntered(MouseEvent e) {}

		/**
		 * Not used.
		 * 
		 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseExited(MouseEvent e) {}
		
		/**
		 * This method check the mouse click location, that is chack if the CardList of the active
		 * player is clicked, if so, determines which card is clicked.
		 * 
		 * @param x
		 * 			x coordinate of the mouse clicked.
		 * 
		 * @param y
		 * 			y coordinate of the mouse clicked.
		 * 
		 * @return
		 * 			An integer indicating which is clicked, if no card, return -1, else returns
		 * 			the index of the card in the cardsInHand.
		 */
		public int checkMouseLocation(int x, int y) {			
			if (activePlayer == -1) {
				return -1; //(j + 3) * getWidth() / 18
			}
			int numOfCards = game.getPlayerList().get(activePlayer).getCardsInHand().size();
			if (x >= bigTwoPanel.getWidth() / 6 && x <= (bigTwoPanel.getWidth() / 6) + (bigTwoPanel.getHeight() / 9) + (numOfCards - 1) * bigTwoPanel.getWidth() / 18 && y >= bigTwoPanel.getHeight() * activePlayer / 5 + 10 && y <= bigTwoPanel.getHeight() * activePlayer / 5 + 30 + bigTwoPanel.getHeight() / 7) {
				int index = 0;
				boolean find = false;
				for (int i = 0; i < numOfCards; i++) {
					if (x >= (i + 3) * bigTwoPanel.getWidth() / 18 && x <= (i + 1 + 3) * bigTwoPanel.getWidth() / 18) {
						index = i;
						find = true;
					}
				}
				if (!find) {
					index = numOfCards - 1;
				}
				if (!selected[index]) {
					if (index > 0) {
						if (selected[index - 1] && y < bigTwoPanel.getHeight() * activePlayer / 5 + 30 && x <= (index - 1 + 3) * bigTwoPanel.getWidth() / 18 + bigTwoPanel.getHeight() / 7) {
							return index - 1;
						}
					}
					if (y >= bigTwoPanel.getHeight() * activePlayer / 5 + 30) {
						return index;
					}
				} else {
					if (y <= bigTwoPanel.getHeight() * activePlayer / 5 + 10 + bigTwoPanel.getHeight() / 7) {
						return index;
					} else if (index == 0 || (index > 0 && selected[index - 1])) {
						return -1;
					} else if (index > 0 && !selected[index - 1]) {
						return index - 1;
					}
				}
			}
			return -1;
		}
	}
	
	/**
	 * This class handles the action event when the play button is clicked. It implements ActionListener
	 * interface.
	 * 
	 * @author Li Gengyu
	 *
	 */
	class PlayButtonListener implements ActionListener {

		/**
		 * This method handle the action event, when the play button is clicked, it just call the 
		 * player to make move.
		 * 
		 * @param e
		 * 			The action event that the play button is clicked.
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			if (game.getCurrentIdx() != ((BigTwoClient) game).getPlayerID()) {
				printMsg("This is not your turn!\n");
			} else {
				game.makeMove(activePlayer, getSelected());
			}	
		}
		
	}
	
	/**
	 * This class handles the action event when the pass button is clicked. It implements ActionListener
	 * interface.
	 * 
	 * @author Li Gengyu
	 *
	 */
	class PassButtonListener implements ActionListener {

		/**
		 * This method handle the action event, when the pass button is clicked, it just call the 
		 * player to make move.
		 * 
		 * @param e
		 * 			The action event that the pass button is clicked.
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			if (game.getCurrentIdx() != ((BigTwoClient) game).getPlayerID()) {
				printMsg("This is not your turn!\n");
			} else {
				resetSelected();
				repaint();
				game.makeMove(activePlayer, getSelected());
			}	
		}
	}
	
	/**
	 * This class handle the action event when the restart item in menu is clicked, it 
	 * implements ActionListener interface.
	 * 
	 * @author Li Gengyu
	 *
	 */
	class ConnectMenuItemListener implements ActionListener {

		/**
		 * This method handle the action event when the restart item in menu is clicked, it just restarts the game.
		 * 
		 * @param e
		 * 			Action event that the the restart item in menu is clicked.
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			((BigTwoClient) game).makeConnection();
		}
	}
	
	/**
	 * This class handle the action event when the quit item in menu is clicked, it 
	 * implements ActionListener interface.
	 * 
	 * @author Li Gengyu
	 *
	 */
	class QuitMenuItemListener implements ActionListener {

		/**
		 * This method handle the action event when the quit item in menu is clicked, it just ends the game.
		 * 
		 * @param e
		 * 			Action event that the the quit item in menu is clicked.
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}
	
	/**
	 * Method to handle the chat input field event when the enter key is hit and sends the chat 
	 * message to the chat area. Will be used for an action listener for the text field.
	 * @author Li Gengyu
	 *
	 */
	class ChatInputListener implements ActionListener {

		/**
		 * The method handle the case when the enter key is hit in text field, to send the message out 
		 * to the chat area.
		 * @param e
		 * 			Action event when the enter key is hit.
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			String content = chatInput.getText();
			if (content != "") {
				chatInput.setText("");
				((BigTwoClient) game).sendMessage(new CardGameMessage(CardGameMessage.MSG, -1, content));
			}
		}
	}
}