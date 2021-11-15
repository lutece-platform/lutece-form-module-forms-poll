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
package fr.paris.lutece.plugins.poll.service;

import fr.paris.lutece.plugins.poll.business.PollForm;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import fr.paris.lutece.plugins.forms.business.FormQuestionResponse;
import fr.paris.lutece.plugins.forms.business.FormQuestionResponseHome;
import fr.paris.lutece.plugins.forms.business.FormResponse;
import fr.paris.lutece.plugins.forms.business.FormResponseHome;
import fr.paris.lutece.plugins.forms.business.Question;
import fr.paris.lutece.plugins.forms.business.QuestionHome;
import fr.paris.lutece.plugins.genericattributes.business.Response;
import fr.paris.lutece.plugins.poll.business.PollData;
import fr.paris.lutece.plugins.poll.business.PollFormHome;
import fr.paris.lutece.plugins.poll.business.PollFormQuestion;
import fr.paris.lutece.plugins.poll.business.PollFormQuestionHome;
import fr.paris.lutece.plugins.poll.business.PollVisualization;

public class PollFormService
{

    public static Map<PollVisualization, List<PollData>> getPollVisualizationList( int nIdPoll )
    {

        Map<PollVisualization, List<PollData>> pollVisualizationWithDataList = new HashMap<>( );

        PollForm pollForm = PollFormHome.findByPrimaryKey( Integer.valueOf( nIdPoll ) );
        List<PollFormQuestion> pollFormQuestionList = PollFormQuestionHome.getPollFormQuestionListByFormId( pollForm.getId( ), pollForm.getIdForm( ) );
        List<FormResponse> listFormResponses = FormResponseHome.selectAllFormResponsesUncompleteByIdForm( pollForm.getIdForm( ) );
        List<FormQuestionResponse> listFormQuestionResponse = FormQuestionResponseHome.getFormQuestionResponseListByFormResponseList(
                listFormResponses.parallelStream( ).map( i -> i.getId( ) ).distinct( ).collect( Collectors.toList( ) ) );

        List<PollFormQuestion> pollFormQuestionListChecked = pollFormQuestionList.stream( )
                .filter( formQuestionResponse -> formQuestionResponse.getIsChecked( ) == true ).collect( Collectors.toList( ) );

        List<Integer> listPollFormQuestionId = pollFormQuestionListChecked.stream( ).map( PollFormQuestion::getIdQuestion ).collect( Collectors.toList( ) );

        List<Question> listQuestions = QuestionHome.findByPrimaryKeyList( listPollFormQuestionId );

        for ( PollFormQuestion pollFormQuestion : pollFormQuestionListChecked )
        {

            Question question = listQuestions.stream( ).filter( x -> x.getId( ) == pollFormQuestion.getIdQuestion( ) ).findFirst( ).orElse( null );

            PollVisualization pollVisualization = new PollVisualization( );
            pollVisualization.setId( question.getId( ) );
            pollVisualization.setTitle( question.getTitle( ) );
            pollVisualization.setType( pollFormQuestion.getType( ) );
            pollVisualization.setIsToolBox( pollFormQuestion.getIsToolbox( ) );
            List<String> responses = new ArrayList<String>( );

            List<FormQuestionResponse> formQuestionResponseList = listFormQuestionResponse.parallelStream( )
                    .filter( x -> x.getQuestion( ).getId( ) == question.getId( ) ).collect( Collectors.toList( ) );

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

            Map<String, Long> counted = responses.stream( ).collect( Collectors.groupingBy( Function.identity( ), Collectors.counting( ) ) );
            List<PollData> listPollData = new ArrayList<>( );

            counted.forEach( ( labelName, size ) -> {
                listPollData.add( new PollData( labelName, (int) (long) size ) );
            } );
            pollVisualizationWithDataList.put( pollVisualization, listPollData );
        }

        return pollVisualizationWithDataList;
    }

}
