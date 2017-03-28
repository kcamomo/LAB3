package PkgException;

import pkgPokerBLL.Deck;

public class DeckException extends Exception {

	Deck deck;
	
	public DeckException(Deck deck) {
		this.deck=deck;
	}

	
}
