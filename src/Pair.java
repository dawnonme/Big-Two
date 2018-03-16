
/**
 * The class for hand Pair in Big Two game. Subclass of Hand.
 * 
 * @author Li Gengyu
 *
 */
public class Pair extends Hand {
	
	/**
	 * The default serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of Pair class, simply call the constructor of the base Hand class.
	 * 
	 * @param player
	 * 				Player who play this hand
	 * 
	 * @param cards
	 * 				List of cards in this hand.
	 */
	public Pair(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}

	/**
	 * Judge the Pair hand is valid or not.
	 * 
	 * @return 
	 * 			A boolean value true if valid, and vice versa.
	 * 
	 * @see Hand#isValid()
	 */
	@Override
	public boolean isValid() {
		if (size() == 2 && getCard(0).rank == getCard(1).rank) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Retrieve which type of hand it is.
	 * 
	 * @return
	 * 			A String object of the class's name,
	 * 
	 * @see Hand#getType()
	 */
	@Override
	public String getType() {
		return "Pair";
	}
		
}
