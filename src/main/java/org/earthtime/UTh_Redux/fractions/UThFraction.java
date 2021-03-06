/*
 * UThFraction.java
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
package org.earthtime.UTh_Redux.fractions;

import java.awt.geom.Path2D;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.earthtime.UPb_Redux.ReduxConstants;
import org.earthtime.UPb_Redux.valueModels.ValueModel;
import org.earthtime.XMLExceptions.BadOrMissingXMLSchemaException;
import org.earthtime.dataDictionaries.MeasuredRatios;
import org.earthtime.dataDictionaries.RadDates;
import org.earthtime.dataDictionaries.UThAnalysisMeasures;
import org.earthtime.dataDictionaries.UThCompositionalMeasures;
import org.earthtime.dataDictionaries.UThFractionationCorrectedIsotopicRatios;
import org.earthtime.dataDictionaries.UncertaintyTypesEnum;
import org.earthtime.exceptions.ETException;
import org.earthtime.fractions.ETFractionInterface;
import org.earthtime.ratioDataModels.AbstractRatiosDataModel;
import org.earthtime.reduxLabData.ReduxLabData;
import org.earthtime.reportViews.ReportRowGUIInterface;
import org.earthtime.xmlUtilities.XMLSerializationI;
import static org.earthtime.UPb_Redux.ReduxConstants.TIME_IN_MILLISECONDS_FROM_1970_TO_2000;

public class UThFraction implements
        UThFractionI,
        ReportRowGUIInterface,
        Serializable,
        XMLSerializationI {

    private static final long serialVersionUID = 5481272675891277083L;
    protected transient boolean selectedInDataTable;
    // Instance variables
    protected String fractionID;
    protected String grainID;
    protected String sampleName;
    protected int aliquotNumber;
    protected boolean legacy;
    protected String imageURL;
    protected Date timeStamp;
    protected int numberOfGrains;
    protected BigDecimal estimatedDate;
    protected ValueModel[] analysisMeasures;
    protected ValueModel[] measuredRatios;
    protected ValueModel[] fractionationCorrectedIsotopeRatios;
    protected ValueModel[] isotopeDates;
    protected ValueModel[] compositionalMeasures;
    protected ValueModel[] sampleIsochronRatios;
    protected AbstractRatiosDataModel physicalConstantsModel; // fraction class has physicalConstantsModelID
    protected AbstractRatiosDataModel detritalUThModel;
    protected long dateTimeMillisecondsOfAnalysis;
    private boolean spikeCalibrationR230_238IsSecular;
    private boolean spikeCalibrationR234_238IsSecular;
    private ValueModel r230Th_238Ufc_rectificationFactor;
    private ValueModel r234U_238Ufc_rectificationFactor;
    private String r230Th_238Ufc_referenceMaterialName;
    private String r234U_238Ufc_referenceMaterialName;

    protected boolean changed;
    protected boolean deleted;
    protected String fractionNotes;
    protected boolean rejected;

    protected boolean filtered;

    private int rgbColor;
    private transient Path2D errorEllipsePath;
    private transient double ellipseRho;

    public UThFraction() {
        this.fractionID = ReduxConstants.DEFAULT_OBJECT_NAME;
        this.grainID = fractionID;
        this.sampleName = ReduxConstants.DEFAULT_OBJECT_NAME;
        this.aliquotNumber = 1;
        this.legacy = false;
        this.imageURL = "http://";
        this.timeStamp = new Date(System.currentTimeMillis());

        this.numberOfGrains = ReduxLabData.getInstance().getDefaultNumberOfGrains();
        this.estimatedDate = BigDecimal.ZERO;

        analysisMeasures = valueModelArrayFactory(UThAnalysisMeasures.getNames(), UncertaintyTypesEnum.ABS.getName());
        measuredRatios = new ValueModel[0];
        fractionationCorrectedIsotopeRatios = valueModelArrayFactory(UThFractionationCorrectedIsotopicRatios.getNames(), UncertaintyTypesEnum.ABS.getName());
        isotopeDates = valueModelArrayFactory(RadDates.getNamesSorted(), UncertaintyTypesEnum.ABS.getName());
        compositionalMeasures = valueModelArrayFactory(UThCompositionalMeasures.getNames(), UncertaintyTypesEnum.ABS.getName());
        sampleIsochronRatios = new ValueModel[0]; //valueModelArrayFactory(DataDictionary.SampleIsochronRatioNames, UncertaintyTypesEnum.ABS.getName());

        physicalConstantsModel = ReduxLabData.getInstance().getDefaultPhysicalConstantsModel();
        detritalUThModel = ReduxLabData.getInstance().getDefaultDetritalUraniumAndThoriumModel();

        dateTimeMillisecondsOfAnalysis = TIME_IN_MILLISECONDS_FROM_1970_TO_2000;

        spikeCalibrationR230_238IsSecular = false;
        spikeCalibrationR234_238IsSecular = false;
        // see fraction reducer for multiplicative use
        this.r230Th_238Ufc_rectificationFactor = new ValueModel(
                "r230Th_238Ufc_rectificationFactor",
                BigDecimal.ONE,
                "ABS",
                BigDecimal.ONE,
                BigDecimal.ONE);
        this.r234U_238Ufc_rectificationFactor = new ValueModel(
                "r234U_238Ufc_rectificationFactor",
                BigDecimal.ONE,
                "ABS",
                BigDecimal.ONE,
                BigDecimal.ONE);
        
        r230Th_238Ufc_referenceMaterialName = "";
        r234U_238Ufc_referenceMaterialName = "";
        
        this.changed = false;
        this.deleted = false;
        this.fractionNotes = "";
        this.rejected = false;

        rgbColor = 0;

    }

    /**
     * @return the fractionID
     */
    @Override
    public String getFractionID() {
        return fractionID;
    }

    /**
     * @param fractionID the fractionID to set
     */
    @Override
    public void setFractionID(String fractionID) {
        this.fractionID = fractionID;
    }

    /**
     * @return the grainID
     */
    @Override
    public String getGrainID() {
        return grainID;
    }

    /**
     * @param grainID the grainID to set
     */
    @Override
    public void setGrainID(String grainID) {
        this.grainID = grainID;
    }

    /**
     * @return the sampleName
     */
    @Override
    public String getSampleName() {
        return sampleName;
    }

    /**
     * @param sampleName the sampleName to set
     */
    @Override
    public void setSampleName(String sampleName) {
        this.sampleName = sampleName;
    }

    /**
     * @return the aliquotNumber
     */
    @Override
    public int getAliquotNumber() {
        return aliquotNumber;
    }

    /**
     * @param aliquotNumber the aliquotNumber to set
     */
    @Override
    public void setAliquotNumber(int aliquotNumber) {
        this.aliquotNumber = aliquotNumber;
    }

    @Override
    public void setLegacy(boolean legacy) {
        this.legacy = legacy;
    }

    /**
     * @return the isLegacy
     */
    @Override
    public boolean isLegacy() {
        return legacy;
    }

    /**
     * @return the imageURL
     */
    @Override
    public String getImageURL() {
        return imageURL;
    }

    /**
     * @param imageURL the imageURL to set
     */
    @Override
    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    /**
     * @return the timeStamp
     */
    @Override
    public Date getTimeStamp() {
        return timeStamp;
    }

    /**
     * @param timeStamp the timeStamp to set
     */
    @Override
    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    /**
     * @return the numberOfGrains
     */
    @Override
    public int getNumberOfGrains() {
        return numberOfGrains;
    }

    /**
     * @param numberOfGrains the numberOfGrains to set
     */
    @Override
    public void setNumberOfGrains(int numberOfGrains) {
        this.numberOfGrains = numberOfGrains;
    }

    /**
     *
     * @return
     */
    @Override
    public BigDecimal getEstimatedDate() {
        return estimatedDate;
    }

    /**
     *
     * @param estimatedDate
     */
    @Override
    public void setEstimatedDate(BigDecimal estimatedDate) {
        this.estimatedDate = estimatedDate;
    }

    /**
     * @return the analysisMeasures
     */
    @Override
    public ValueModel[] getAnalysisMeasures() {
        return analysisMeasures;
    }

    /**
     * @param analysisMeasures the analysisMeasures to set
     */
    @Override
    public void setAnalysisMeasures(ValueModel[] analysisMeasures) {
        this.analysisMeasures = analysisMeasures;
    }

    /**
     * @return the measuredRatios
     */
    @Override
    public ValueModel[] getMeasuredRatios() {
        return measuredRatios;
    }

    /**
     * @param measuredRatios the measuredRatios to set
     */
    @Override
    public void setMeasuredRatios(ValueModel[] measuredRatios) {
        this.measuredRatios = measuredRatios;
    }

    /**
     * @return the fractionationCorrectedIsotopeRatios
     */
    @Override
    public ValueModel[] getRadiogenicIsotopeRatios() {
        return fractionationCorrectedIsotopeRatios;
    }

    /**
     * @param fractionationCorrectedIsotopeRatios the
     * fractionationCorrectedIsotopeRatios to set
     */
    @Override
    public void setRadiogenicIsotopeRatios(ValueModel[] fractionationCorrectedIsotopeRatios) {
        this.fractionationCorrectedIsotopeRatios = fractionationCorrectedIsotopeRatios;
    }

    /**
     * @return the isotopeDates
     */
    @Override
    public ValueModel[] getIsotopeDates() {
        return isotopeDates;
    }

    /**
     * @param isotopeDates the isotopeDates to set
     */
    @Override
    public void setIsotopeDates(ValueModel[] isotopeDates) {
        this.isotopeDates = isotopeDates;
    }

    /**
     * @return the compositionalMeasures
     */
    @Override
    public ValueModel[] getCompositionalMeasures() {
        return compositionalMeasures;
    }

    /**
     * @param compositionalMeasures the compositionalMeasures to set
     */
    @Override
    public void setCompositionalMeasures(ValueModel[] compositionalMeasures) {
        this.compositionalMeasures = compositionalMeasures;
    }

    /**
     * @return the sampleIsochronRatios
     */
    @Override
    public ValueModel[] getSampleIsochronRatios() {
        return sampleIsochronRatios;
    }

    /**
     * @param sampleIsochronRatios the sampleIsochronRatios to set
     */
    @Override
    public void setSampleIsochronRatios(ValueModel[] sampleIsochronRatios) {
        this.sampleIsochronRatios = sampleIsochronRatios;
    }

    /**
     * @return the changed
     */
    @Override
    public boolean isChanged() {
        return changed;
    }

    /**
     * @param changed the changed to set
     */
    @Override
    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    /**
     * @return the deleted
     */
    @Override
    public boolean isDeleted() {
        return deleted;
    }

    /**
     * @param deleted the deleted to set
     */
    @Override
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * @return the fractionNotes
     */
    @Override
    public String getFractionNotes() {
        return fractionNotes;
    }

    /**
     * @param fractionNotes the fractionNotes to set
     */
    @Override
    public void setFractionNotes(String fractionNotes) {
        this.fractionNotes = fractionNotes;
    }

    /**
     * @return the rejected
     */
    @Override
    public boolean isRejected() {
        return rejected;
    }

    /**
     * @param rejected the rejected to set
     */
    @Override
    public void setRejected(boolean rejected) {
        this.rejected = rejected;
    }

    @Override
    public void toggleRejectedStatus() {
        this.rejected = !this.rejected;
    }

    /**
     * @return the traceElements
     */
    @Override
    public ValueModel[] getTraceElements() {
        return new ValueModel[0];
    }

    /**
     * @param traceElements the traceElements to set
     */
    @Override
    public void setTraceElements(ValueModel[] traceElements) {
    }

    @Override
    public boolean isSelectedInDataTable() {
        return selectedInDataTable;
    }

    @Override
    public void setSelectedInDataTable(boolean selectedInDataTable) {
        this.selectedInDataTable = selectedInDataTable;
    }

    @Override
    public boolean isStandard() {
        return false;
    }

    @Override
    public void setStandard(boolean standard) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the secondaryStandard
     */
    @Override
    public boolean isSecondaryStandard() {
        return false;
    }

    /**
     * @param secondaryStandard the secondaryStandard to set
     */
    @Override
    public void setSecondaryStandard(boolean secondaryStandard) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     * @return
     */
    @Override
    public AbstractRatiosDataModel getPhysicalConstantsModel() {
        if (physicalConstantsModel == null) {
            physicalConstantsModel = ReduxLabData.getInstance().getDefaultPhysicalConstantsModel();
        }
        return physicalConstantsModel;
    }

    /**
     *
     * @param physicalConstantsModel
     */
    @Override
    public void setPhysicalConstantsModel(AbstractRatiosDataModel physicalConstantsModel) {
        if ((this.physicalConstantsModel == null)
                || (!this.physicalConstantsModel.equals(physicalConstantsModel))) {
            this.physicalConstantsModel = physicalConstantsModel;
            this.setChanged(true);
//            System.out.println(this.getFractionID() //
//                    + "  is getting new physical constants model = "//
//                    + physicalConstantsModel.getNameAndVersion());
        }
    }

    /**
     *
     * @return
     */
    @Override
    public AbstractRatiosDataModel getDetritalUThModel() {
        if (detritalUThModel == null) {
            detritalUThModel = ReduxLabData.getInstance().getDefaultDetritalUraniumAndThoriumModel();
        }
        return detritalUThModel;
    }

    /**
     *
     * @param detritalUThModel
     * @param physicalConstantsModel
     */
    @Override
    public void setDetritalUThModel(AbstractRatiosDataModel detritalUThModel) {
        if ((this.detritalUThModel == null)
                || (!this.detritalUThModel.equals(detritalUThModel))) {
            this.detritalUThModel = detritalUThModel;
            this.setChanged(true);
        }
    }

    @Override
    public String getAnalysisFractionComment() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setAnalysisFractionComment(String analysisFractionComment) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ValueModel getMeasuredRatioByName(String ratioName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ValueModel getMeasuredRatioByName(MeasuredRatios myMeasuredRatio) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getPhysicalConstantsModelID() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getRatioType() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setRatioType(String RatioType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setEllipseRho(double ellipseRho) {
        this.ellipseRho = ellipseRho;
    }

    @Override
    public double getEllipseRho() {
        return ellipseRho;
    }

    @Override
    public void setErrorEllipsePath(Path2D errorEllipsePath) {
        this.errorEllipsePath = errorEllipsePath;
    }

    @Override
    public Path2D getErrorEllipsePath() {
        return errorEllipsePath;
    }

    @Override
    public void setRgbColor(int rgbColor) {
        this.rgbColor = rgbColor;
    }

    @Override
    public int getRgbColor() {
        return rgbColor;
    }

    @Override
    public void getValuesFrom(ETFractionInterface fraction, boolean copyAnalysisMeasures) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void serializeXMLObject(String filename) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object readXMLObject(String filename, boolean doValidate) throws FileNotFoundException, ETException, FileNotFoundException, BadOrMissingXMLSchemaException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isFiltered() {
        return filtered;
    }

    @Override
    public void setFiltered(boolean filtered) {
        this.filtered = filtered;
    }

    @Override
    public ValueModel getTracerRatioByName(String trName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ValueModel getPbBlankRatioByName(String trName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ValueModel getInitialPbModelRatioByName(String trName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the dateTimeMillisecondsOfAnalysis
     */
    public long getDateTimeMillisecondsOfAnalysis() {
        return dateTimeMillisecondsOfAnalysis;
    }

    /**
     * @param dateTimeMillisecondsOfAnalysis the dateTimeMillisecondsOfAnalysis
     * to set
     */
    public void setDateTimeMillisecondsOfAnalysis(long dateTimeMillisecondsOfAnalysis) {
        this.dateTimeMillisecondsOfAnalysis = dateTimeMillisecondsOfAnalysis;
    }

    /**
     * @return the spikeCalibrationR230_238IsSecular
     */
    public boolean isSpikeCalibrationR230_238IsSecular() {
        return spikeCalibrationR230_238IsSecular;
    }

    /**
     * @param spikeCalibrationR230_238IsSecular the
     * spikeCalibrationR230_238IsSecular to set
     */
    public void setSpikeCalibrationR230_238IsSecular(boolean spikeCalibrationR230_238IsSecular) {
        this.spikeCalibrationR230_238IsSecular = spikeCalibrationR230_238IsSecular;
    }

    /**
     * @return the spikeCalibrationR234_238IsSecular
     */
    public boolean isSpikeCalibrationR234_238IsSecular() {
        return spikeCalibrationR234_238IsSecular;
    }

    /**
     * @param spikeCalibrationR234_238IsSecular the
     * spikeCalibrationR234_238IsSecular to set
     */
    public void setSpikeCalibrationR234_238IsSecular(boolean spikeCalibrationR234_238IsSecular) {
        this.spikeCalibrationR234_238IsSecular = spikeCalibrationR234_238IsSecular;
    }

    @Override
    public ValueModel[] getLegacyActivityRatios() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setLegacyActivityRatios(ValueModel[] compositionalMeasures) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the referenceMaterialRectificationFactor
     */
    public ValueModel getR230Th_238Ufc_rectificationFactor() {
        return r230Th_238Ufc_rectificationFactor;
    }

    /**
     * @param referenceMaterialRectificationFactor the
     * referenceMaterialRectificationFactor to set
     */
    public void setR230Th_238Ufc_rectificationFactor(ValueModel r230Th_238Ufc_rectificationFactor) {
        this.r230Th_238Ufc_rectificationFactor = r230Th_238Ufc_rectificationFactor;
    }

    /**
     * @return the r234U_238Ufc_rectificationFactor
     */
    public ValueModel getR234U_238Ufc_rectificationFactor() {
        return r234U_238Ufc_rectificationFactor;
    }

    /**
     * @param r234U_238Ufc_rectificationFactor the
 r234U_238Ufc_rectificationFactor to set
     */
    public void setR234U_238Ufc_rectificationFactor(ValueModel r234U_238Ufc_rectificationFactor) {
        this.r234U_238Ufc_rectificationFactor = r234U_238Ufc_rectificationFactor;
    }

    /**
     * @return the r230Th_238Ufc_referenceMaterialName
     */
    public String getR230Th_238Ufc_referenceMaterialName() {
        return r230Th_238Ufc_referenceMaterialName;
    }

    /**
     * @param r230Th_238Ufc_referenceMaterialName the r230Th_238Ufc_referenceMaterialName to set
     */
    public void setR230Th_238Ufc_referenceMaterialName(String r230Th_238Ufc_referenceMaterialName) {
        this.r230Th_238Ufc_referenceMaterialName = r230Th_238Ufc_referenceMaterialName;
    }

    /**
     * @return the r234U_238Ufc_referenceMaterialName
     */
    public String getR234U_238Ufc_referenceMaterialName() {
        return r234U_238Ufc_referenceMaterialName;
    }

    /**
     * @param r234U_238Ufc_referenceMaterialName the r234U_238Ufc_referenceMaterialName to set
     */
    public void setR234U_238Ufc_referenceMaterialName(String r234U_238Ufc_referenceMaterialName) {
        this.r234U_238Ufc_referenceMaterialName = r234U_238Ufc_referenceMaterialName;
    }

}
