package leshy.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import leshy.cards.abstracts.TotemHeadCard;
import leshy.powers.TotemPower;

public class TotemHeadAction extends AbstractGameAction {

    private final AbstractPlayer p = AbstractDungeon.player;

    private final TotemHeadCard card;

    public TotemHeadAction(TotemHeadCard card){
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.POWER;
        this.card = card;
    }
    
    @Override
    public void update() {

        for(AbstractPower ap : AbstractDungeon.player.powers){
            if(ap instanceof TotemPower){
                updateTotem( (TotemPower) ap);
                ap.updateDescription();
                this.isDone = true;
                return;
            }
        }

        TotemPower totem = new TotemPower(AbstractDungeon.player);
        updateTotem(totem);
        totem.updateDescription();
        addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, totem));

        this.isDone = true;

    }

    public void updateTotem(TotemPower t){
        t.setTribe(card.giveTribe());
    }

}













