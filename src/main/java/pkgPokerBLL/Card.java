package pkgPokerBLL;

import java.util.Comparator;

import pkgPokerEnum.*;

public class Card implements Comparable {

	private eRank eRank;
	private eSuit eSuit;
	private boolean Wild;
	private int iCardNbr;
	
	public Card()
	{
		
	}
	public Card(eRank eRank, eSuit eSuit,  int iCardNbr) {
		super();
		this.eSuit = eSuit;
		this.eRank = eRank;
		this.iCardNbr = iCardNbr;
	}
	
	public Card(eRank eRank, eSuit eSuit) {
		this.eRank = eRank;
		this.eSuit = eSuit;
		this.Wild=false;
	}
	
	public Card(eSuit eSuit, eRank eRank, int iCardNbr) {
		this.eRank = eRank;
		this.eSuit = eSuit;
		this.Wild=false;
	}
	
	public Card(eSuit eSuit, eRank eRank, Boolean Wild, int iCardNbr) {
		this.eRank = eRank;
		this.eSuit = eSuit;
		this.Wild=Wild;
	}

	public eSuit geteSuit() {
		return eSuit;
	}

	public eRank geteRank() {
		return eRank;
	}

	public int getiCardNbr() {
		return iCardNbr;
	}

	void seteSuit(eSuit eSuit) {
		this.eSuit = eSuit;
	}
	void seteRank(eRank eRank) {
		this.eRank = eRank;
	}

	
	public boolean isWild() {
		return Wild;
	}
	public void setWild(boolean wild) {
		Wild = wild;
	}


	public static Comparator<Card> CardRank = new Comparator<Card>() {

		public int compare(Card c1, Card c2) {

		   int Cno1 = c1.geteRank().getiRankNbr();
		   int Cno2 = c2.geteRank().getiRankNbr();

		   /*For descending order*/
		   return Cno2 - Cno1;

	   }};

	public int compareTo(Object o) {
	    Card c = (Card) o; 
	    return c.geteRank().compareTo(this.geteRank()); 

	}
}