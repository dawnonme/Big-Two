
/**
 * The class for hand Single in Big Two game. Subclass of Hand.
 * 
 * @author Li Gengyu
 *
 */
public class Triple extends Hand {

	/**
	 * The default serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of Triple class, simply call the constructor of the base Hand class.
	 * 
	 * @param player
	 * 				Player who play this hand
	 * 
	 * @param cards
	 * 				List of cards in this hand.
	 */
	public Triple(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * Judge the Triple hand is valid or not.
	 * 
	 * @return 
	 * 			A boolean value true if valid, and vice versa.
	 * 
	 * @see Hand#isValid()
	 */
	@Override
	public boolean isValid() {
		if (size() == 3 && getCard(0).rank == getCard(1).rank && getCard(0).rank == getCard(2).rank) {
			return true;
		}
		return false;
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
		return "Triple";
	}	
}
