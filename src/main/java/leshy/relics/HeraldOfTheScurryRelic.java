package leshy.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.helpers.PowerTip;
import leshy.LeshyMod;
import leshy.cards.abstracts.AbstractCreatureCard;
import leshy.relics.interfaces.CreatureSigilRelic;
import leshy.util.TextureLoader;

import java.util.HashSet;

import static leshy.LeshyMod.makeRelicOutlinePath;
import static leshy.LeshyMod.makeRelicPath;

public class HeraldOfTheScurryRelic extends CustomRelic implements CreatureSigilRelic {

    public static final String ID = LeshyMod.makeID(HeraldOfTheScurryRelic.class.getSimpleName());

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("herald_of_the_scurry_relic.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("herald_of_the_scurry_relic.png"));

    public HeraldOfTheScurryRelic() {
        super(ID, IMG, OUTLINE, RelicTier.RARE, LandingSound.CLINK);

        PowerTip remove = null;
        for(PowerTip tip : tips){
            if(tip.header.equals("Strike")){
                remove = tip;
                break;
            }
        }
        if(remove != null)
            tips.remove(remove);

        PowerTip ds = AbstractCreatureCard.getSigilPowertip(AbstractCreatureCard.Sigils.DOUBLE_STRIKE);
        if(ds != null)
            tips.add(ds);

        PowerTip l = AbstractCreatureCard.getSigilPowertip(AbstractCreatureCard.Sigils.LEADER);
        if(l != null)
            tips.add(l);

        PowerTip r = AbstractCreatureCard.getSigilPowertip(AbstractCreatureCard.Sigils.RAMPAGER);
        if(r != null)
            tips.add(r);

    }

    @Override
    public String getUpdatedDescription() {

        return DESCRIPTIONS[0];

    }

    @Override
    public HashSet<AbstractCreatureCard.Sigils> giveSigils(AbstractCreatureCard c) {

        HashSet<AbstractCreatureCard.Sigils> sigils = new HashSet<>();

        if(c.tribe == AbstractCreatureCard.CreatureTribe.SQUIRREL || c.tribe == AbstractCreatureCard.CreatureTribe.AMALGAM){
            sigils.add(AbstractCreatureCard.Sigils.DOUBLE_STRIKE);
            sigils.add(AbstractCreatureCard.Sigils.LEADER);
            sigils.add(AbstractCreatureCard.Sigils.RAMPAGER);
        }

        return sigils;

    }

}
