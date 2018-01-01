package com.adpdigital.push.rn;

import com.adpdigital.push.PushMessage;

/**
 * Created by mohammad on 12/24/17.
 */

public interface EventListener {
    void onEvent(PushMessage push);
}
