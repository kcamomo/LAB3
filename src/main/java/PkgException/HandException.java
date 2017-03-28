package PkgException;

import pkgPokerBLL.Hand;

public class HandException extends Exception {
	
	private Hand hand;

	public HandException(Hand hand) {
		this.hand=hand;
	}

	public Hand getHand()
	{
		return hand;
	}

}
