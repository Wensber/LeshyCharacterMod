package leshy.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import leshy.LeshyMod;
import leshy.cards.abstracts.AbstractCreatureCard;
import leshy.characters.Leshy;

import static leshy.LeshyMod.makeCardPath;

public class Geck extends AbstractCreatureCard {


    public static final String ID = LeshyMod.makeID(Geck.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("Geck.png");


    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;


    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = Leshy.Enums.CREATURE;
    public static final CardColor COLOR = Leshy.Enums.LESHY_BROWN;

    private static final int COST = -2;

    public Geck() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);

        sigilImgPath = makeCardPath("Geck_Sigil.png");

        costType = CreatureCostType.NONE;
        extraCost = 0;

        tribe = CreatureTribe.REPTILE;

        attack = baseAttack = trueBaseAttack = 5;

        health = baseHealth = trueBaseHealth = 5;

        initializeDescription();

    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    }

}
