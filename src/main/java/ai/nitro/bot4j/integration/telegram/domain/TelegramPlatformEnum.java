/*
 * Copyright (C) 2016, nitro.ai
 * All rights reserved.
 *
 * This software may be modified and distributed under the terms
 * of the BSD 3-clause license. See the LICENSE file for details.
 */

package ai.nitro.bot4j.integration.telegram.domain;

import ai.nitro.bot4j.middle.domain.Platform;

public enum TelegramPlatformEnum implements Platform {
	TELEGRAM;

	@Override
	public boolean isAsync() {
		return true;
	}

	@Override
	public boolean isVoice() {
		return false;
	}
}
