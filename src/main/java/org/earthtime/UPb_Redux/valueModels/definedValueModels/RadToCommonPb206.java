/*
 * RadToCommonPb206.java
 *
 * Created on Feb 28, 2009
 *
 *
 * Copyright 2006-2017 James F. Bowring and www.Earth-Time.org
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.earthtime.UPb_Redux.valueModels.definedValueModels;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.concurrent.ConcurrentMap;
import org.earthtime.UPb_Redux.ReduxConstants;
import org.earthtime.UPb_Redux.valueModels.ValueModel;

/**
 *
 * @author James F. Bowring
 */
public class RadToCommonPb206 extends ValueModel implements
        Comparable<ValueModel>,
        Serializable {

    // Class variables
    private static final long serialVersionUID = -9215509650885544749L;
    private final static String NAME = "radToCommonPb206";
    private final static String UNCT_TYPE = "ABS";
    // Instance variables
    private ValueModel molPb206r;
    private ValueModel molPb206b;
    private ValueModel molPb206c;
    private ValueModel molPb206t;

    /** Creates a new instance of RadToCommonPb206 */
    public RadToCommonPb206 () {
        super( NAME, UNCT_TYPE );
    }

    /**
     * 
     * @param inputValueModels
     * @param parDerivTerms
     */
    @Override
    public void calculateValue (
            ValueModel[] inputValueModels,
            ConcurrentMap<String, BigDecimal> parDerivTerms ) {


        molPb206r = inputValueModels[0];
        molPb206b = inputValueModels[1];
        molPb206c = inputValueModels[2];
        molPb206t = inputValueModels[3];

        try {
            setValue(molPb206r.getValue().//
                    divide(molPb206r.getValue().//
                    add( molPb206b.getValue() ).//
                    add( molPb206c.getValue() ).//
                    add( molPb206t.getValue() ), ReduxConstants.mathContext15 ) );
        } catch (Exception e) {
            setValue( BigDecimal.ZERO );
        }

    }
}
