package cinema.enums;

public enum GenreType {
    HoatHinh,
    GiaDinh,
    Hai, 
    PhieuLuu,
    HanhDong, 
    KinhDi,
    LangMan;

    public static String getNameGenreType(GenreType value) {
        switch(value) {
            case GenreType.HoatHinh: 
                return "Hoạt Hình";
            case GenreType.GiaDinh:
                return "Gia Đình";
            case GenreType.Hai:
                return "Hài";
            case GenreType.PhieuLuu:
                return "Phiêu Lưu";
            case GenreType.HanhDong:
                return "Hành Động";
            case GenreType.KinhDi:
                return "Kinh Dị";
            case GenreType.LangMan:
                return "Lãng mạn";
            default:
                return null;
        }
    }
}
