
/**
 * This class is designed to model the deck used in Big Two game, which is the subclass of Deck class.
 * 
 * @author Li Gengyu
 *
 */
public class BigTwoDeck extends Deck {
	/**
	 * This is a default serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * This method is an override method from initialize method in Deck class. This method clear all the 
	 * Cards in list and create 52 BigTwoCard objects then adds them to ist.
	 * 
	 * @see Deck#initialize()
	 */
	@Override
	public void initialize() {
		removeAllCards();
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 13; j++) {
				BigTwoCard card = new BigTwoCard(i, j);
				addCard(card);
			}
		}
	}	
}
