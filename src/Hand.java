
/**
 * This class is designed to model all the hands valid in the Big Two game. As instantiation of a Hand is not
 * meaningful, I declare it abstract. It is the subclass of CardList class because a hand is composed of a list
 * card. It is the base class of all the specific hand class.
 * 
 * @author Li Gengyu
 *
 */
public abstract class Hand extends CardList {
	
	/**
	 * This is a default serial version UID.
	 */
	private static final long serialVersionUID = 1L;
	private CardGamePlayer player; // The player who plays this hand

	/**
	 * Constructor of Hand class, which set the player and list of card of this hand.
	 * 
	 * @param player
	 * 				The player who plays this hand.
	 * 
	 * @param cards
	 * 				The list of Cards to compose the hand.
	 */
	public Hand(CardGamePlayer player, CardList cards) {
		this.player = player;
		for (int i = 0; i < cards.size(); i++) {
			addCard(cards.getCard(i));
		}
	}
	
	/**
	 * @return
	 */
	public CardGamePlayer getPlayer() {
		return player;
	}
	
	/**
	 * Method to retrieve the top card according to the compareTo method, of this hand, usually used to compare
	 * to same type hands.
	 * 
	 * @return
	 * 			Return the top card in the hand. Or in the card list.
	 */
	public Card getTopCard() {
		sort();
		if (!isEmpty()) {
			return getCard(size() - 1);
		}
		return null;
	}
	
	/**
	 * Method to judge whether this hand beats that hand. 
	 * 
	 * @param hand
	 * 				Another hand for comparing.
	 * 
	 * @return
	 * 			A boolean value to indicate whether this hand beats that hand or not, true if beats,
	 *          and vice versa.
	 */
	public boolean beats(Hand hand) {
		if (getType() == hand.getType() && getTopCard().compareTo(hand.getTopCard()) == 1) {
			return true;
		}
		return false;
	}
	
	/**
	 * Abstract method which will be implemented by the subclasses to indicate whether this type of hand is valid.
	 * 
	 * @return
	 * 			A boolean value to indicate whether this type of hand is valid or not, true if valid and vice versa.
	 */
	public abstract boolean isValid();
	
	/**
	 * Abstract method which will be implemented by the subclasses to indicate which type of hand it is.
	 * 
	 * @return
	 * 			A String object of the hand's name.
	 */
	public abstract String getType();
}
