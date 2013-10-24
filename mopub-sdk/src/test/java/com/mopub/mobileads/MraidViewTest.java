/*
 * Copyright (c) 2010-2013, MoPub Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 *  Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 *  Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 *  Neither the name of 'MoPub Inc.' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.mopub.mobileads;

import android.app.Activity;
import android.content.Intent;
import android.webkit.WebViewClient;
import com.mopub.mobileads.test.support.SdkTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;

@RunWith(SdkTestRunner.class)
public class MraidViewTest {

    private MraidView subject;
    private AdConfiguration adConfiguration;
    private WebViewClient webViewClient;

    @Before
    public void setUp() throws Exception {
        adConfiguration = mock(AdConfiguration.class);
        subject = new MraidView(new Activity(), adConfiguration);
        webViewClient = subject.getMraidWebViewClient();
    }

    @Test
    public void loadHtmlData_whenDataIsNull_shouldNotBlowUp() throws Exception {
        subject.loadHtmlData(null);
        // pass
    }

    @Test
    public void shouldOverrideUrlLoading_withMraidCommandCreateCalendarEvent_withoutUserClick_shouldNotOpenNewIntent() throws Exception {
        String url = "mraid://createCalendarEvent?description=Mayan%20Apocalypse%2FEnd%20of%20World&start=2013-08-16T20%3A00-04%3A00&interval=1&frequency=daily";

        webViewClient.shouldOverrideUrlLoading(null, url);

        assertThat(Robolectric.getShadowApplication().getNextStartedActivity()).isNull();
    }

    @Test
    public void shouldOverrideUrlLoading_withMraidCommandPlayVideo_withUserClick_shouldOpenNewIntent() throws Exception {
        String url = "mraid://playVideo?uri=something";
        subject.onUserClick();

        webViewClient.shouldOverrideUrlLoading(null, url);

        Intent startedIntent = Robolectric.getShadowApplication().getNextStartedActivity();
        assertThat(startedIntent).isNotNull();

        assertThat(startedIntent.getComponent().getClassName()).isEqualTo("com.mopub.mobileads.MraidVideoPlayerActivity");
    }

    @Test
    public void shouldOverrideUrlLoading_withRedirectUrl_withoutUserClick_shouldNotOpenNewIntent() throws Exception {
        String url = "http://www.blah.com";

        webViewClient.shouldOverrideUrlLoading(null, url);

        Intent startedIntent = Robolectric.getShadowApplication().getNextStartedActivity();
        assertThat(startedIntent).isNull();
    }

    @Test
    public void shouldOverrideUrlLoading_withRedirectUrl_withUserClick_shouldOpenNewIntent() throws Exception {
        String url = "http://www.blah.com";
        subject.onUserClick();

        webViewClient.shouldOverrideUrlLoading(null, url);

        Intent startedIntent = Robolectric.getShadowApplication().getNextStartedActivity();
        assertThat(startedIntent).isNotNull();
    }
}
