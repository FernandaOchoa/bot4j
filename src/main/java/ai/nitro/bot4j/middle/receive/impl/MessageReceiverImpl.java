/*
 * Copyright (C) 2016, nitro.ai
 * All rights reserved.
 *
 * This software may be modified and distributed under the terms
 * of the BSD 3-clause license. See the LICENSE file for details.
 */

package ai.nitro.bot4j.middle.receive.impl;

import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ai.nitro.bot4j.bot.Bot;
import ai.nitro.bot4j.integration.alexa.domain.AlexaPlatformEnum;
import ai.nitro.bot4j.middle.domain.Participant;
import ai.nitro.bot4j.middle.domain.Platform;
import ai.nitro.bot4j.middle.domain.receive.ReceiveMessage;
import ai.nitro.bot4j.middle.receive.DuplicateMessageFilter;
import ai.nitro.bot4j.middle.receive.MessageReceiver;

public class MessageReceiverImpl implements MessageReceiver {

	final static Logger LOG = LogManager.getLogger(MessageReceiverImpl.class);

	@Inject
	protected Bot bot;

	@Inject
	protected DuplicateMessageFilter duplicateMessageFilter;

	protected void handleReceiveMessage(final ReceiveMessage receiveMessage) {
		try {
			final boolean isDuplicateMessage = duplicateMessageFilter.isDuplicate(receiveMessage);

			if (isDuplicateMessage) {
				LOG.info("ignoring duplicate message {}", receiveMessage);
			} else {
				bot.onMessage(receiveMessage);
			}
		} catch (final Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}

	protected boolean isPlatformAsync(final Platform platform) {
		final boolean result = !AlexaPlatformEnum.ALEXA.equals(platform);
		return result;
	}

	@Override
	public void receive(final ReceiveMessage receiveMessage) {
		final Participant sender = receiveMessage.getSender();
		final Platform platform = sender.getPlatform();
		final boolean isPlatformAsync = isPlatformAsync(platform);

		if (isPlatformAsync) {
			CompletableFuture.runAsync(() -> handleReceiveMessage(receiveMessage));
		} else {
			handleReceiveMessage(receiveMessage);
		}
	}
}
