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

import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.portal.util.mvc.xpage.MVCApplication;
import fr.paris.lutece.plugins.forms.business.FormHome;
import fr.paris.lutece.plugins.forms.business.FormQuestionResponse;
import fr.paris.lutece.plugins.forms.business.FormQuestionResponseHome;
import fr.paris.lutece.plugins.forms.business.FormResponse;
import fr.paris.lutece.plugins.forms.business.FormResponseHome;
import fr.paris.lutece.plugins.forms.business.Question;
import fr.paris.lutece.plugins.forms.business.QuestionHome;
import fr.paris.lutece.plugins.forms.service.FormService;
import fr.paris.lutece.plugins.genericattributes.business.Response;
import fr.paris.lutece.plugins.poll.business.PollData;
import fr.paris.lutece.plugins.poll.business.PollForm;
import fr.paris.lutece.plugins.poll.business.PollFormHome;
import fr.paris.lutece.plugins.poll.business.PollFormQuestion;
import fr.paris.lutece.plugins.poll.business.PollFormQuestionHome;
import fr.paris.lutece.plugins.poll.business.PollVisualization;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.util.mvc.xpage.annotations.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

/**
 * This class provides a simple implementation of an XPage
 */
@Controller( xpageName = "pollform", pageTitleI18nKey = "poll.xpage.pollform.pageTitle", pagePathI18nKey = "poll.xpage.pollform.pagePathLabel" )
public class PollFormApp extends MVCApplication
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String TEMPLATE_XPAGE = "/skin/plugins/poll/pollform.html";
    private static final String VIEW_HOME = "home";
    private static final String PARAMETER_ID_POLL = "id_poll";

    /**
     * Returns the content of the page pollform.
     * 
     * @param request
     *            The HTTP request
     * @return The view
     */
    @View( value = VIEW_HOME, defaultView = true )
    public XPage viewHome( HttpServletRequest request )
    {
        Map<String, Object> model = getModel( );
        Map<PollVisualization, List<PollData>> pollVisualizationWithDataList = new HashMap<>( );

        String strIdPoll = request.getParameter( PARAMETER_ID_POLL );
        if ( strIdPoll != null )
        {
            PollForm pollForm = PollFormHome.findByPrimaryKey( Integer.valueOf( strIdPoll ) );
            if ( pollForm.getIsVisible( ) )
            {
                List<PollFormQuestion> pollFormQuestionList = PollFormQuestionHome.getPollFormQuestionListByFormId( pollForm.getId( ), pollForm.getIdForm( ) );
                for ( PollFormQuestion pollFormQuestion : pollFormQuestionList )
                {
                    if ( pollFormQuestion.getIsChecked( ) )
                    {
                        Question question = QuestionHome.findByPrimaryKey( pollFormQuestion.getIdQuestion( ) );
                        PollVisualization pollVisualization = new PollVisualization( );
                        pollVisualization.setId( question.getId( ) );
                        pollVisualization.setTitle( question.getTitle( ) );
                        pollVisualization.setType( pollFormQuestion.getType( ) );
                        pollVisualization.setIsToolBox( pollFormQuestion.getIsToolbox( ) );
                        List<FormResponse> listFormResponses = FormResponseHome.selectAllFormResponsesUncompleteByIdForm( pollForm.getIdForm( ) );
                        List<String> responses = new ArrayList<String>( );
                        for ( FormResponse formResponse : listFormResponses )
                        {
                            if ( !formResponse.isFromSave( ) )
                            {
                                List<FormQuestionResponse> formQuestionResponseList = FormQuestionResponseHome
                                        .findFormQuestionResponseByResponseQuestion( formResponse.getId( ), pollFormQuestion.getIdQuestion( ) );
                                for ( FormQuestionResponse formQuestionResponse : formQuestionResponseList )
                                {
                                    List<Response> responseList = formQuestionResponse.getEntryResponse( );
                                    for ( Response response : responseList )
                                    {
                                        if ( response.getField( ) != null )
                                        {
                                            responses.add( response.getResponseValue( ) );
                                        }
                                    }
                                }
                            }
                        }

                        Map<String, Long> counted = responses.stream( ).collect( Collectors.groupingBy( Function.identity( ), Collectors.counting( ) ) );
                        List<PollData> listPollData = new ArrayList<>( );

                        counted.forEach( ( labelName, size ) -> {
                            listPollData.add( new PollData( labelName, (int) (long) size ) );
                        } );

                        pollVisualizationWithDataList.put( pollVisualization, listPollData );
                    }

                }
            }
            model.put( "poll_form", pollForm );
        }
        model.put( "poll_visualization_list", pollVisualizationWithDataList );
        return getXPage( TEMPLATE_XPAGE, getLocale( request ), model );
    }

}
