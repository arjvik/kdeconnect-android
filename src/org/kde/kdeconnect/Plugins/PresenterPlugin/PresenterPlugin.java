/*
 * SPDX-FileCopyrightText: 2014 Ahmed I. Khalil <ahmedibrahimkhali@gmail.com>
 *
 * SPDX-License-Identifier: GPL-2.0-only OR GPL-3.0-only OR LicenseRef-KDE-Accepted-GPL
*/

package org.kde.kdeconnect.Plugins.PresenterPlugin;


import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;

import androidx.core.content.ContextCompat;

import org.apache.commons.lang3.ArrayUtils;
import org.kde.kdeconnect.NetworkPacket;
import org.kde.kdeconnect.Plugins.MousePadPlugin.MousePadPlugin;
import org.kde.kdeconnect.Plugins.Plugin;
import org.kde.kdeconnect.Plugins.PluginFactory;
import org.kde.kdeconnect_tp.R;

import static org.kde.kdeconnect.Plugins.MousePadPlugin.KeyListenerView.SpecialKeysMap;

@PluginFactory.LoadablePlugin
public class PresenterPlugin extends Plugin {
    
    private final static String PACKET_TYPE_MOUSEPAD_REQUEST = MousePadPlugin.PACKET_TYPE_MOUSEPAD_REQUEST;

    public boolean isPointerSupported() {
        return device.supportsPacketType(PACKET_TYPE_MOUSEPAD_REQUEST);
    }

    @Override
    public String getDisplayName() {
        return context.getString(R.string.pref_plugin_presenter);
    }

    @Override
    public String getDescription() {
        return context.getString(R.string.pref_plugin_presenter_desc);
    }

    @Override
    public Drawable getIcon() {
        return ContextCompat.getDrawable(context, R.drawable.ic_presenter_24dp);
    }

    @Override
    public boolean hasSettings() {
        return false;
    }

    @Override
    public boolean hasMainActivity() {
        return true;
    }

    @Override
    public void startMainActivity(Activity parentActivity) {
        Intent intent = new Intent(parentActivity, PresenterActivity.class);
        intent.putExtra("deviceId", device.getDeviceId());
        parentActivity.startActivity(intent);
    }

    @Override
    public String[] getSupportedPacketTypes() {  return ArrayUtils.EMPTY_STRING_ARRAY; }

    @Override
    public String[] getOutgoingPacketTypes() {
        return new String[]{PACKET_TYPE_MOUSEPAD_REQUEST};
    }

    @Override
    public String getActionName() {
        return context.getString(R.string.pref_plugin_presenter);
    }

    public void sendNext() {
       NetworkPacket np = new NetworkPacket(PACKET_TYPE_MOUSEPAD_REQUEST);
        np.set("singleclick", true);
        device.sendPacket(np);
    }

    public void sendPrevious() {
        NetworkPacket np = new NetworkPacket(PACKET_TYPE_MOUSEPAD_REQUEST);
        np.set("rightclick", true);
        device.sendPacket(np);
    }

    public void sendFullscreen() {
        NetworkPacket np = new NetworkPacket(PACKET_TYPE_MOUSEPAD_REQUEST);
        np.set("specialKey", SpecialKeysMap.get(KeyEvent.KEYCODE_F5));
        device.sendPacket(np);
    }

    public void sendEsc() {
        NetworkPacket np = new NetworkPacket(PACKET_TYPE_MOUSEPAD_REQUEST);
        np.set("specialKey", SpecialKeysMap.get(KeyEvent.KEYCODE_ESCAPE));
        device.sendPacket(np);
    }

    public void sendPointer(float xDelta, float yDelta) {
        NetworkPacket np = device.getAndRemoveUnsentPacket(NetworkPacket.PACKET_REPLACEID_MOUSEMOVE);
        if (np == null) {
            np = new NetworkPacket(PACKET_TYPE_MOUSEPAD_REQUEST);
        } else {
            xDelta += np.getInt("dx");
            yDelta += np.getInt("dy");
        }
        np.set("dx", xDelta);
        np.set("dy", yDelta);
        device.sendPacket(np, NetworkPacket.PACKET_REPLACEID_MOUSEMOVE);
    }

    public void stopPointer() {}
}
