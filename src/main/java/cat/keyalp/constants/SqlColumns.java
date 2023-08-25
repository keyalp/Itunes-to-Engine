package cat.keyalp.constants;

import java.sql.Types;

public enum SqlColumns {
    BPM("bpm", Types.INTEGER),
    YEAR("year", Types.INTEGER),
    BITRATE("bitrate", Types.INTEGER),
    BPM_ANALYZED("bpmAnalyzed", Types.REAL),
    TITLE("title", Types.VARCHAR),
    ARTIST("artist", Types.VARCHAR),
    ALBUM("album", Types.VARCHAR),
    GENRE("genre", Types.VARCHAR),
    COMMENT("comment", Types.VARCHAR),
    LABEL("label", Types.VARCHAR),
    COMPOSER("composer", Types.VARCHAR),
    REMIXER("remixer", Types.VARCHAR),
    KEY("key", Types.INTEGER),
    RATING("rating", Types.INTEGER),
    ALBUM_ART("albumArt", Types.VARCHAR),
    TIME_LAST_PLAYED("timeLastPlayed", Types.TIMESTAMP),
    FILE_TYPE("fileType", Types.VARCHAR),
    IS_ANALYZED("isAnalyzed", Types.BOOLEAN),
    DATE_CREATED("dateCreated", Types.TIMESTAMP),
    DATE_ADDED("dateAdded", Types.TIMESTAMP),
    IS_AVAILABLE("isAvailable", Types.BOOLEAN),
    PLAYED_INDICATOR("playedIndicator", Types.INTEGER),
    IS_METADATA_IMPORTED("isMetadataImported", Types.BOOLEAN),
    PDB_IMPORTED_KEY("pdbImportKey", Types.INTEGER),
    URI("uri", Types.VARCHAR),
    IS_BEAT_GRID_LOCKED("isBeatGridLocked", Types.BOOLEAN),
    TRACK_DATA("trackData", Types.BLOB),
    OVERVIEW_WAVEFORM_DATA("overviewWaveFormData", Types.BLOB),
    BEAT_DATA("beatData", Types.BLOB),
    QUICK_CUES("quickCues", Types.BLOB),
    LOOPS("loops", Types.BLOB),
    LAST_EDIT_TIME("lastEditTime", Types.TIMESTAMP),
    ACTIVE_ON_LOAD_LOOPS("activeOnLoadLoops", Types.INTEGER);


    private final String columnName;
    private final int dataType;

    SqlColumns(String columnName, int dataType) {
        this.columnName = columnName;
        this.dataType = dataType;
    }

    public String getColumnName() {
        return columnName;
    }

    public int getDataType() {
        return dataType;
    }
}
