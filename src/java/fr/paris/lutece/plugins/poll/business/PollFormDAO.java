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
 * This class provides Data Access methods for PollForm objects
 */
public final class PollFormDAO implements IPollFormDAO
{
    // Constants
    private static final String SQL_QUERY_SELECT = "SELECT id_poll_form, id_form, is_visible, title, btn_title, btn_url, btn_is_visible FROM poll_form WHERE id_poll_form = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO poll_form ( id_form, is_visible, title, btn_title, btn_url, btn_is_visible ) VALUES ( ?, ?, ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM poll_form WHERE id_poll_form = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE poll_form SET id_poll_form = ?, id_form = ?, is_visible = ?, title = ?, btn_title = ?, btn_url = ?, btn_is_visible = ? WHERE id_poll_form = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_poll_form, id_form, is_visible, title, btn_title, btn_url, btn_is_visible FROM poll_form";
    private static final String SQL_QUERY_SELECTALL_ID = "SELECT id_poll_form FROM poll_form";

    /**
     * {@inheritDoc }
     */
    @Override
    public void insert( PollForm pollForm, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, Statement.RETURN_GENERATED_KEYS, plugin ) )
        {
            int nIndex = 1;
            daoUtil.setInt( nIndex++, pollForm.getIdForm( ) );
            daoUtil.setBoolean( nIndex++, pollForm.getIsVisible( ) );
            daoUtil.setString( nIndex++, pollForm.getTitle( ) );
            daoUtil.setString( nIndex++, pollForm.getBtnTitle( ) );
            daoUtil.setString( nIndex++, pollForm.getBtnURL( ) );
            daoUtil.setBoolean( nIndex++, pollForm.getBtnIsVisible( ) );

            daoUtil.executeUpdate( );
            if ( daoUtil.nextGeneratedKey( ) )
            {
                pollForm.setId( daoUtil.getGeneratedKeyInt( 1 ) );
            }
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public PollForm load( int nKey, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin ) )
        {
            daoUtil.setInt( 1, nKey );
            daoUtil.executeQuery( );
            PollForm pollForm = null;

            if ( daoUtil.next( ) )
            {
                pollForm = new PollForm( );
                int nIndex = 1;

                pollForm.setId( daoUtil.getInt( nIndex++ ) );
                pollForm.setIdForm( daoUtil.getInt( nIndex++ ) );
                pollForm.setIsVisible( daoUtil.getBoolean( nIndex++ ) );
                pollForm.setTitle( daoUtil.getString( nIndex++ ) );
                pollForm.setBtnTitle( daoUtil.getString( nIndex++ ) );
                pollForm.setBtnURL( daoUtil.getString( nIndex++ ) );
                pollForm.setBtnIsVisible( daoUtil.getBoolean( nIndex ) );

            }

            daoUtil.free( );
            return pollForm;
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
    public void store( PollForm pollForm, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin ) )
        {
            int nIndex = 1;

            daoUtil.setInt( nIndex++, pollForm.getId( ) );
            daoUtil.setInt( nIndex++, pollForm.getIdForm( ) );
            daoUtil.setBoolean( nIndex++, pollForm.getIsVisible( ) );
            daoUtil.setString( nIndex++, pollForm.getTitle( ) );
            daoUtil.setString( nIndex++, pollForm.getBtnTitle( ) );
            daoUtil.setString( nIndex++, pollForm.getBtnURL( ) );
            daoUtil.setBoolean( nIndex++, pollForm.getBtnIsVisible( ) );

            daoUtil.setInt( nIndex, pollForm.getId( ) );

            daoUtil.executeUpdate( );
            daoUtil.free( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<PollForm> selectPollFormsList( Plugin plugin )
    {
        List<PollForm> pollFormList = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin ) )
        {
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                PollForm pollForm = new PollForm( );
                int nIndex = 1;

                pollForm.setId( daoUtil.getInt( nIndex++ ) );
                pollForm.setIdForm( daoUtil.getInt( nIndex++ ) );
                pollForm.setIsVisible( daoUtil.getBoolean( nIndex++ ) );
                pollForm.setTitle( daoUtil.getString( nIndex++ ) );
                pollForm.setBtnTitle( daoUtil.getString( nIndex++ ) );
                pollForm.setBtnURL( daoUtil.getString( nIndex++ ) );
                pollForm.setBtnIsVisible( daoUtil.getBoolean( nIndex ) );

                pollFormList.add( pollForm );
            }

            daoUtil.free( );
            return pollFormList;
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<Integer> selectIdPollFormsList( Plugin plugin )
    {
        List<Integer> pollFormList = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ID, plugin ) )
        {
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                pollFormList.add( daoUtil.getInt( 1 ) );
            }

            daoUtil.free( );
            return pollFormList;
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public ReferenceList selectPollFormsReferenceList( Plugin plugin )
    {
        ReferenceList pollFormList = new ReferenceList( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin ) )
        {
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                pollFormList.addItem( daoUtil.getInt( 1 ), daoUtil.getString( 2 ) );
            }

            daoUtil.free( );
            return pollFormList;
        }
    }
}
