package leshy.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import leshy.LeshyMod;
import leshy.util.TextureLoader;

import static leshy.LeshyMod.makePowerPath;

//Gain 1 dex for the turn for each card played.

public class BrokenEggPower extends AbstractPower implements CloneablePowerInterface{
    public AbstractCreature source;

    public static final String POWER_ID = LeshyMod.makeID(BrokenEggPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("broken_egg_power84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("broken_egg_power32.png"));

    public BrokenEggPower(final AbstractCreature owner, final AbstractCreature source, final int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.source = source;
        this.amount = amount;

        this.type = PowerType.DEBUFF;

        // We load those txtures here.
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        if (type == DamageInfo.DamageType.NORMAL) {
            float multi = 1.0f - 0.2f*this.amount;
            return damage * multi;
        }
        return damage;
    }

    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
        if (info.type == DamageInfo.DamageType.NORMAL && damageAmount > 0){
            addToTop(new ReducePowerAction(this.owner, this.owner, this, 1));
            return 0;
        }
        return damageAmount;
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        if(this.amount >= 4)
            this.amount = 4;
    }

    @Override
    public void updateDescription() {

        int percent = 20 * this.amount;

        if(this.amount == 1)
            description = DESCRIPTIONS[0] + percent + DESCRIPTIONS[1] + DESCRIPTIONS[2] + DESCRIPTIONS[4];
        else
            description = DESCRIPTIONS[0] + percent + DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[3] + DESCRIPTIONS[4];

    }


    @Override
    public AbstractPower makeCopy() {
        return new BrokenEggPower(owner, source, amount);
    }

}
