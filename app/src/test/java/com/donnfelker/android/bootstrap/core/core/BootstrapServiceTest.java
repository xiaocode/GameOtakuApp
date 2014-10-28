package com.gameotaku.app.core.core;

import com.gameotaku.app.core.CheckIn;
import com.gameotaku.app.core.News;
import com.gameotaku.app.core.User;
import com.gameotaku.app.core.UserAgentProvider;
import com.gameotaku.app.service.BootstrapService;
import com.github.kevinsawicki.http.HttpRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;

/**
 * Unit tests of {@link com.gameotaku.app.service.BootstrapService}
 */
@RunWith(MockitoJUnitRunner.class)
public class BootstrapServiceTest {

    @Mock
    private HttpRequest request;
    private BootstrapService service;

    /**
     * Create reader for string
     *
     * @param value
     * @return input stream reader
     * @throws IOException
     */
    private static BufferedReader createReader(String value) throws IOException {
        return new BufferedReader(new InputStreamReader(new ByteArrayInputStream(
                value.getBytes(HttpRequest.CHARSET_UTF8))));
    }

    /**
     * Set up default mocks
     *
     * @throws IOException
     */
    @Before
    public void before() throws IOException {
        service = new BootstrapService("foo", new UserAgentProvider()) {
            protected HttpRequest execute(HttpRequest request) throws IOException {
                return BootstrapServiceTest.this.request;
            }
        };
        doReturn(true).when(request).ok();
    }

    /**
     * Verify getting users with an empty response
     *
     * @throws IOException
     */
    @Test
    public void getUsersEmptyResponse() throws IOException {
        doReturn(createReader("")).when(request).bufferedReader();
        List<User> users = service.getUsers();
        assertNotNull(users);
        assertTrue(users.isEmpty());
    }

    /**
     * Verify getting news with an empty response
     *
     * @throws IOException
     */
    @Test
    public void getContentEmptyResponse() throws IOException {
        doReturn(createReader("")).when(request).bufferedReader();
        List<News> content = service.getNews();
        assertNotNull(content);
        assertTrue(content.isEmpty());
    }

    /**
     * Verify getting checkins with an empty response
     *
     * @throws IOException
     */
    @Test
    public void getReferrersEmptyResponse() throws IOException {
        doReturn(createReader("")).when(request).bufferedReader();
        List<CheckIn> referrers = service.getCheckIns();
        assertNotNull(referrers);
        assertTrue(referrers.isEmpty());
    }
}
