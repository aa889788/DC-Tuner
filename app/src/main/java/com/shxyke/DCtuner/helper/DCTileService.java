package com.shxyke.DCtuner.helper;

import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

import com.shxyke.DCtuner.Contents;
import com.shxyke.DCtuner.utils.SPUtils;

public class DCTileService extends TileService {

    @Override
    public void onTileAdded() {
        super.onTileAdded();
    }

    @Override
    public void onTileRemoved() {
        super.onTileRemoved();
    }

    @Override
    public void onClick() {
        int state = getQsTile().getState();
        if (state == Tile.STATE_INACTIVE) {
            // 更改成非活跃状态(还有一个参数：STATE_UNAVAILABLE 非可点击状态)
            Utilties.set_dc_status(true);
        } else {
            //更改成活跃状态
            Utilties.set_dc_status(false);
        }
        SPUtils.putData(Contents.SP_DC_DIMMING_STATUS_KEY, Utilties.get_dc_status());
        if((boolean)SPUtils.getData(Contents.SP_DC_DIMMING_STATUS_KEY, false)){
            getQsTile().setState(Tile.STATE_ACTIVE);
        }else{
            getQsTile().setState(Tile.STATE_INACTIVE);
        }
        //可以点击设置图标，设置方式如下：
        //Icon icon = Icon.createWithResource(getApplicationContext(), R.drawable.xxxx);
        //getQsTile().setIcon(icon);
        //设置label：
        //getQsTile.setLabel("");
        //更新Tile
        this.getQsTile().updateTile();
    }

    @Override
    public void onStartListening() {
        // 打开下拉通知栏的时候调用,当快速设置按钮并没有在编辑栏拖到设置栏中不会调用
        // 在TleAdded之后会调用一次
        if((boolean) SPUtils.getData(Contents.SP_DC_DIMMING_STATUS_KEY, false)){
            getQsTile().setState(Tile.STATE_ACTIVE);
        }else{
            getQsTile().setState(Tile.STATE_INACTIVE);
        }
        getQsTile().updateTile();
    }

    @Override
    public void onStopListening () {
        // 关闭下拉通知栏的时候调用,当快速设置按钮并没有在编辑栏拖到设置栏中不会调用
        // 在onTileRemoved移除之前也会调用移除
        super.onStopListening();
    }

}
