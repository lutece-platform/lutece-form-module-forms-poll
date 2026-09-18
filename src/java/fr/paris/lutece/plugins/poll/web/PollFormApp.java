/*
 * Copyright (c) 2002-2021, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.poll.web;

import fr.paris.lutece.portal.service.message.SiteMessage;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.message.SiteMessageService;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.portal.util.mvc.xpage.MVCApplication;
import fr.paris.lutece.plugins.poll.business.PollForm;
import fr.paris.lutece.plugins.poll.business.PollFormHome;
import fr.paris.lutece.plugins.poll.service.PollFormService;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.util.mvc.xpage.annotations.Controller;
import fr.paris.lutece.portal.web.cdi.mvc.Models;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.math.NumberUtils;

/**
 * This class provides a simple implementation of an XPage
 */
@RequestScoped
@Named( "poll.xpage.pollform" )
@Controller( xpageName = "pollform", pageTitleI18nKey = "poll.xpage.pollform.pageTitle", pagePathI18nKey = "poll.xpage.pollform.pagePathLabel", securityTokenEnabled = true )
public class PollFormApp extends MVCApplication
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String TEMPLATE_XPAGE = "/skin/plugins/poll/pollform.html";
    private static final String VIEW_HOME = "home";
    private static final String PARAMETER_ID_POLL = "id_poll";
    private static final String MESSAGE_ERROR_POLL_NOT_FOUND = "poll.message.error.pollNotFound";

    /**
     * Returns the content of the page pollform.
     * 
     * @param request
     *            The HTTP request
     * @return The view
     */
    @View( value = VIEW_HOME, defaultView = true )
    public XPage viewHome( HttpServletRequest request, Models model ) throws SiteMessageException
    {
        int nIdPoll = NumberUtils.toInt( request.getParameter( PARAMETER_ID_POLL ), -1 );
        PollForm pollForm = ( nIdPoll != -1 ) ? PollFormHome.findByPrimaryKey( nIdPoll ) : null;

        if ( pollForm == null )
        {
            SiteMessageService.setMessage( request, MESSAGE_ERROR_POLL_NOT_FOUND, SiteMessage.TYPE_ERROR );
            return null;
        }

        model.put( "poll_form", pollForm );
        model.put( "poll_visualization_list", PollFormService.getPollVisualizationList( nIdPoll ) );

        return getXPage( TEMPLATE_XPAGE, getLocale( request ) );
    }

}
