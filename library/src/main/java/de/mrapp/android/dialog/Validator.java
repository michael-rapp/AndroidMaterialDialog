/*
 * AndroidMaterialDialog Copyright 2014 - 2015 Michael Rapp
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 */
package de.mrapp.android.dialog;

/**
 * Defines the interface, a class, which should be able to validate the content of a dialog, must
 * implement.
 *
 * @author Michael Rapp
 * @since 3.0.0
 */
public interface Validator {

    /**
     * Validates the content of a dialog.
     *
     * @return True, if the content of the dialog is valid, false otherwise
     */
    boolean validate();

}