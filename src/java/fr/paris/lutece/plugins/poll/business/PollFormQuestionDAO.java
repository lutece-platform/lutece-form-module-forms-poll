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

package fr.paris.lutece.plugins.poll.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.sql.DAOUtil;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides Data Access methods for PollFormQuestion objects
 */
public final class PollFormQuestionDAO implements IPollFormQuestionDAO
{
    // Constants
    private static final String SQL_QUERY_SELECT = "SELECT id_poll_form_question, id_poll_form, id_form, id_question, chart_type, chart_is_toolbox, chart_is_checked FROM poll_form_question WHERE id_poll_form_question = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO poll_form_question ( id_poll_form, id_form, id_question, chart_type, chart_is_toolbox, chart_is_checked ) VALUES ( ?, ?, ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM poll_form_question WHERE id_poll_form_question = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE poll_form_question SET id_poll_form_question = ?, id_poll_form = ?, id_form = ?, id_question = ?, chart_type = ?, chart_is_toolbox = ?, chart_is_checked = ? WHERE id_poll_form_question = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_poll_form_question, id_poll_form, id_form, id_question, chart_type, chart_is_toolbox, chart_is_checked FROM poll_form_question";
    private static final String SQL_QUERY_SELECTALL_ID = "SELECT id_poll_form_question FROM poll_form_question";
    private static final String SQL_QUERY_SELECTALL_BY_FORM_ID = "SELECT id_poll_form_question, id_poll_form, id_form, id_question, chart_type, chart_is_toolbox, chart_is_checked FROM poll_form_question WHERE id_poll_form = ? AND id_form = ?";
    private static final String SQL_QUERY_SELECT_BY_QUESTION_ID = "SELECT id_poll_form_question, id_poll_form, id_form, id_question, chart_type, chart_is_toolbox, chart_is_checked FROM poll_form_question WHERE id_poll_form = ? AND id_question = ?";

    /**
     * {@inheritDoc }
     */
    @Override
    public void insert( PollFormQuestion pollFormQuestion, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, Statement.RETURN_GENERATED_KEYS, plugin ) )
        {
            int nIndex = 1;
            daoUtil.setInt( nIndex++, pollFormQuestion.getIdPollForm( ) );
            daoUtil.setInt( nIndex++, pollFormQuestion.getIdForm( ) );
            daoUtil.setInt( nIndex++, pollFormQuestion.getIdQuestion( ) );
            daoUtil.setString( nIndex++, pollFormQuestion.getType( ) );
            daoUtil.setBoolean( nIndex++, pollFormQuestion.getIsToolbox( ) );
            daoUtil.setBoolean( nIndex++, pollFormQuestion.getIsChecked( ) );

            daoUtil.executeUpdate( );
            if ( daoUtil.nextGeneratedKey( ) )
            {
                pollFormQuestion.setId( daoUtil.getGeneratedKeyInt( 1 ) );
            }
        }

    }

    /**
     * {@inheritDoc }
     */
    @Override
    public PollFormQuestion load( int nKey, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin ) )
        {
            daoUtil.setInt( 1, nKey );
            daoUtil.executeQuery( );
            PollFormQuestion pollFormQuestion = null;

            if ( daoUtil.next( ) )
            {
                pollFormQuestion = new PollFormQuestion( );
                int nIndex = 1;

                pollFormQuestion.setId( daoUtil.getInt( nIndex++ ) );
                pollFormQuestion.setIdPollForm( daoUtil.getInt( nIndex++ ) );
                pollFormQuestion.setIdForm( daoUtil.getInt( nIndex++ ) );
                pollFormQuestion.setIdQuestion( daoUtil.getInt( nIndex++ ) );
                pollFormQuestion.setType( daoUtil.getString( nIndex++ ) );
                pollFormQuestion.setIsToolBox( daoUtil.getBoolean( nIndex++ ) );
                pollFormQuestion.setIsChecked( daoUtil.getBoolean( nIndex ) );

            }

            daoUtil.free( );
            return pollFormQuestion;
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void delete( int nKey, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin ) )
        {
            daoUtil.setInt( 1, nKey );
            daoUtil.executeUpdate( );
            daoUtil.free( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void store( PollFormQuestion pollFormQuestion, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin ) )
        {
            int nIndex = 1;

            daoUtil.setInt( nIndex++, pollFormQuestion.getId( ) );
            daoUtil.setInt( nIndex++, pollFormQuestion.getIdPollForm( ) );
            daoUtil.setInt( nIndex++, pollFormQuestion.getIdForm( ) );
            daoUtil.setInt( nIndex++, pollFormQuestion.getIdQuestion( ) );
            daoUtil.setString( nIndex++, pollFormQuestion.getType( ) );
            daoUtil.setBoolean( nIndex++, pollFormQuestion.getIsToolbox( ) );
            daoUtil.setBoolean( nIndex++, pollFormQuestion.getIsChecked( ) );
            daoUtil.setInt( nIndex, pollFormQuestion.getId( ) );

            daoUtil.executeUpdate( );
            daoUtil.free( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<PollFormQuestion> selectPollFormQuestionsList( Plugin plugin )
    {
        List<PollFormQuestion> pollFormQuestionList = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin ) )
        {
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                PollFormQuestion pollFormQuestion = new PollFormQuestion( );
                int nIndex = 1;

                pollFormQuestion.setId( daoUtil.getInt( nIndex++ ) );
                pollFormQuestion.setIdPollForm( daoUtil.getInt( nIndex++ ) );
                pollFormQuestion.setIdForm( daoUtil.getInt( nIndex++ ) );
                pollFormQuestion.setIdQuestion( daoUtil.getInt( nIndex++ ) );
                pollFormQuestion.setType( daoUtil.getString( nIndex++ ) );
                pollFormQuestion.setIsToolBox( daoUtil.getBoolean( nIndex++ ) );
                pollFormQuestion.setIsChecked( daoUtil.getBoolean( nIndex ) );

                pollFormQuestionList.add( pollFormQuestion );
            }

            daoUtil.free( );
            return pollFormQuestionList;
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<Integer> selectIdPollFormQuestionsList( Plugin plugin )
    {
        List<Integer> pollFormQuestionList = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ID, plugin ) )
        {
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                pollFormQuestionList.add( daoUtil.getInt( 1 ) );
            }

            daoUtil.free( );
            return pollFormQuestionList;
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public ReferenceList selectPollFormQuestionsReferenceList( Plugin plugin )
    {
        ReferenceList pollFormQuestionList = new ReferenceList( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin ) )
        {
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                pollFormQuestionList.addItem( daoUtil.getInt( 1 ), daoUtil.getString( 2 ) );
            }

            daoUtil.free( );
            return pollFormQuestionList;
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<PollFormQuestion> selectPollFormQuestionListByFormId( int nIdPollForm, int nFormId, Plugin plugin )
    {
        List<PollFormQuestion> pollFormQuestionList = new ArrayList<PollFormQuestion>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_BY_FORM_ID, plugin ) )
        {
            daoUtil.setInt( 1, nIdPollForm );
            daoUtil.setInt( 2, nFormId );
            daoUtil.executeQuery( );
            while ( daoUtil.next( ) )
            {
                PollFormQuestion pollFormQuestion = new PollFormQuestion( );
                int nIndex = 1;
                pollFormQuestion.setId( daoUtil.getInt( nIndex++ ) );
                pollFormQuestion.setIdPollForm( daoUtil.getInt( nIndex++ ) );
                pollFormQuestion.setIdForm( daoUtil.getInt( nIndex++ ) );
                pollFormQuestion.setIdQuestion( daoUtil.getInt( nIndex++ ) );
                pollFormQuestion.setType( daoUtil.getString( nIndex++ ) );
                pollFormQuestion.setIsToolBox( daoUtil.getBoolean( nIndex++ ) );
                pollFormQuestion.setIsChecked( daoUtil.getBoolean( nIndex++ ) );
                pollFormQuestionList.add( pollFormQuestion );
            }
        }
        return pollFormQuestionList;
    }

    /**
     * {@inheritDoc }
     */
    public PollFormQuestion loadByQuestionId( int nIdPollForm, int nQuestionId, Plugin plugin )
    {
        PollFormQuestion pollFormQuestion = null;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_QUESTION_ID, plugin ) )
        {
            daoUtil.setInt( 1, nIdPollForm );
            daoUtil.setInt( 2, nQuestionId );
            daoUtil.executeQuery( );
            if ( daoUtil.next( ) )
            {
                pollFormQuestion = new PollFormQuestion( );
                int nIndex = 1;
                pollFormQuestion.setId( daoUtil.getInt( nIndex++ ) );
                pollFormQuestion.setIdPollForm( daoUtil.getInt( nIndex++ ) );
                pollFormQuestion.setIdForm( daoUtil.getInt( nIndex++ ) );
                pollFormQuestion.setIdQuestion( daoUtil.getInt( nIndex++ ) );
                pollFormQuestion.setType( daoUtil.getString( nIndex++ ) );
                pollFormQuestion.setIsToolBox( daoUtil.getBoolean( nIndex++ ) );
                pollFormQuestion.setIsChecked( daoUtil.getBoolean( nIndex++ ) );
            }
        }
        return pollFormQuestion;
    }
}
