 package fr.paris.lutece.plugins.poll.web;

import fr.paris.lutece.plugins.poll.business.PollForm;
import fr.paris.lutece.plugins.poll.business.PollFormHome;
import fr.paris.lutece.portal.web.cdi.mvc.Models;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;

import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

/**
 * This is the application class test for the object pollform
 */
public class PollFormAppTest extends LuteceTestCase
{
    @Inject
    private PollFormApp _xpage;
    @Inject
    private Models _models;

    @Test
    public void testXPage( ) throws SiteMessageException
    {
        // Create a poll form to display
        PollForm pollForm = new PollForm( );
        pollForm.setIdForm( 1 );
        pollForm.setIsVisible( true );
        pollForm.setTitle( "Test poll" );
        PollFormHome.create( pollForm );

        try
        {
            // Xpage display test
            MockHttpServletRequest request = new MockHttpServletRequest( );
            request.addParameter( "id_poll", String.valueOf( pollForm.getId( ) ) );

            XPage xpage = _xpage.viewHome( request, _models );

            assertNotNull( xpage );
            assertTrue( xpage.getContent( ).contains( "Test poll" ) );
        }
        finally
        {
            PollFormHome.remove( pollForm.getId( ) );
        }
    }

    @Test
    public void testXPageUnknownPoll( )
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( "id_poll", "999999" );

        assertThrows( SiteMessageException.class, ( ) -> _xpage.viewHome( request, _models ) );
    }

}
