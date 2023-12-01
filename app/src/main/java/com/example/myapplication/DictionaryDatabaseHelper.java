package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
public class DictionaryDatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "dictionary.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "words";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_WORD = "word";
    public static final String COLUMN_DEFINITION = "definition";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_WORD + " TEXT, " +
                    COLUMN_DEFINITION + " TEXT)";


    public DictionaryDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void insertTip(SQLiteDatabase db, String category, String tip) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_WORD, category); // 여기서는 카테고리로 단어를 사용
        values.put(COLUMN_DEFINITION, tip);

        // 데이터베이스에 데이터 추가
        db.insert(TABLE_NAME, null, values);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
        // 정비 팁 추가 예시
        insertTip(db, "타이어", "1.동전 확인법\n"+
                "이순신 장군이 보이는 동전의 뒷면에서 감투를 아래로 향하게 하고 타이어 트레드에 넣어 감투가 얼마나 보이는지 확인하여 트레드의 높이를 확인하는 방법으로, 감투가 보이지 않는다면 상태가 좋은 거고, 감투의 절반 이상이 보이면 교체 시기로 인지하시면 됩니다.\n" +
                "감투가 안보이면 상태 GOOD, 교체할 필요 없음\n" +
                "감투가 절반 이상 보이면 교체 시기!\n"+
                "2.카드 확인법\n"+
                "카드 리더기에 결제하듯이 마그네틱을 아래 방향으로 향하게 하고 타이어 트레드에 넣어 카드의 마그네틱 부분과 카드의 끝과의 간격이 얼마나 보이는지 확인하여 트레드의 높이를 확인하시면 됩니다.\n"+
                "마그네틱이 일부 가려지거나, 카드 간격이 보이지 않으면 교체할 필요 없음\n" +
                "카드 간격이 보이면 교체 시기!");
        insertTip(db, "엔진오일", "엔진오일은 누구나 간단히 점검할 수 있습니다.\n " +
                "차량을 평탄한 곳에 두고 시동을 끈 다음 5분 경과 후 엔진오일 체크기를 뽑았을 때, 레벨 게이지 F와 L사이에 오일 표시가 있으면 정상입니다.\n " +
                "게이지가 E에 가깝게 오일이 줄어들었거나 색이 지나치게 검게 변했을 때, 점도가 처음 교환했을 때와는 다르게 많이 묽어졌다면 교체를 해야 할 시기입니다.\n" +
                "단, 디젤 엔진오일은 새것으로 교환한 직후에도 검은색이므로 교환주기를 참고해야 합니다.\n");
        insertTip(db, "브레이크 패드", "핸들을 끝까지 돌려서 패드 마모상태를 직접 눈으로 확인해보실 수 있습니다\n" +
                "휠 사이로 고개를 집어넣고 확인하긴 힘들기 때문에 스마트 폰으로 사진을 찍어 확대하신 후 확인하는 게 좋습니다.\n" +
                "휠 안쪽에 브레이크 패드의 디스크 부분이 얼마나 많이 달았는지 두께를 보면 알 수가 있습니다.\n" +
                "브레이크는 패드가 달려있는 캘리퍼가 디스크를 잡아주면서 작동을 하게 되는데 이때 브레이크 패드가 마모가 되면서 수명을 줄이는 형식이기 때문입니다.\n "+
                "패드의 두께가 얇아져서 캘리퍼와 디스크가 거의 맞닿게 되는 부분까지 와 있다면 패드의 교체 시기로 볼 수 있는 것입니다.\n"+
                "또는 운전 중 브레이크를 밟았을 때 평소와 깊게 밟히는 느낌이 들 때 패드의 교체 주기가 다 되었을 가능성이 높습니다.\n"+
                "패드가 많이 마모되었다는 것은 디스크와 패드 간의 간격이 그만 큼 벌어졌다는 뜻이고 이때 같은 브레이크 동작을 하기 위해서는 평소보다 브레이크를 깊게 밟아줘야 하기 때문입니다.\n");
        insertTip(db, "브레이크 로터와 드럼", "브레이크 로터와 드럼은 브레이크 디스크라고도 합니다.\n" +
                "브레이크 디스크는 외관에 노출되어 있어서 온도 변화에 따른 열변형이 올 수 있고 급제동을 많이하다보면 마모가 심하게 될 수도 있습니다.\n" +
                "그럴 경우 아래와 같은 현상이 발생할 수 있으며 그럴 경우 디스크 점검을 받아보셔야 합니다.\n" +
                "1. 편제동 발생 : 디스크 면이 고르게 마모가 되지 않은 편마모 상태가 발생하면 편제동이 발생할 수 있습니다.\n" +
                "2. 제동력 저하 : 디스크로터의 외관에 스크레치가 많으면 제동력이 떨어집니다.\n" +
                "3. 과도한 소음 : 디스크의 상처면에 이물질이 들어가면 제동 시 소음이 심할 수 있습니다.\n" +
                "4. 브레이크 페달을 밟을때 떨림 발생 : 디스크 표면이 매끄럽지 않아 발생함. (교체 또는 연마 필요)\n" +
                "5, 핸들떨림핸들 떨림 : 디스크에 열변형이 발생하면 핸들 떨림이 발생합니다. (그래서 세차할 때 차량을 충분히 식힌 후 물을 뿌려라고 합니다.)\n ");
        insertTip(db, "브레이크 오일", "브레이크 오일의 수분 함량이 3% 이상일 시 교체를 해주는 게 좋다고 알려져 있습니다.\n "+
                "브레이크 오일이 담긴 오일 리저버 탱크는 차 앞유리 아래 엔진룸에 있습니다.\n"+
                "브레이크 오일은 반드시 같은 등급 제품을 사용해야 하고 옅은 노란색,진한 노란색,갈색 순으로 변성이 진행됩니다.\n"+
                "만약 갈색이나 검은색에 가깝다면 교체시기라고 판단하셔야 합니다.\n");
        insertTip(db, "냉각수", "부동액 냉각수에는 빨간색이나 녹색 등의 색상이 지정되어 있습니다.\n"+
                "또한 최근 신차에 파란색과 분홍색의 냉각수가 채워져 있습니다. 파란색과 분홍색(핑크색)의 부동액은 빨간색이나 녹색과 달리 수명이 긴 것입니다.\n"+
                "일반적으로 적색 또는 녹색의 냉각수 교환시기는 2 년(약 40,000km), 유럽 제조 모델에 사용하는 노란색 5년(약 20만 km), 파란색과 분홍색(핑크)는 7 년 ~ 10 년(20만 km 이상) 정도로 되어 있습니다.\n" +
                "항상 동일한 유형과 브랜드의 냉각수를 차에 채우십시오. 내 차의 냉각수 유형을 잘 모르시는 분은 저장 탱크에 들어있는 색상을 확인한 후 같은 색상의 냉각수를 구입하는 것이 좋습니다.\n "+
                "부동액 교환 시 자신의 차에 맞는 부동액 색깔을 확인하시고 부동액의 색깔이 검게 변했거나  원래 부동액 제품의 색과 달라진 경우에는 부동액(냉각수)의 교환이 필요하며 냉각계통에 대한 점검도 필요할 수 있습니다.\n" +
                "차량의 냉각수를 세척하고 교체하는 시기에 대한보다 정확한 정보는 차량 소유자 매뉴얼을 참조하십시오.");
        insertTip(db, "배터리", "자동차 배터리 수명과 교체 주기를 판단하는 방법에는 진단 장비를 사용하는 방법이 있고 차량 증상을 통해 추정하는 방법이 있습니다.\n" +
                "진단 장비를 사용하는 경우, 진단기를  배터리의 음극과 양극에 연결하여 저항값을 측정하는 방법을 사용합니다. \n" +
                "이 방법으로 수명을 체크했을 때 70% 이하인 경우 교체하는 것을 권장하고 있습니다. \n" +
                "또한 배터리의 수명이 다 되었을 때 나타나는 증상을 통해 교체 주기를 판단할 수 있습니다. \n" +
                "예를 들어  액셀러레이터를 밟는 정도에 비례하여 헤드라이트의 광량이 줄어들거나 혹은 깜빡일 수 있으며, \n" +
                "시동을 걸었을 때 시동음이 작고 약해지며 둔탁하게 변하고\n" +
                "또 클랙슨 소리가 이전에 비해 약해지거나\n" +
                "완충을 시켰음에도 인디케이터 표시에 초록불이 들어오지 않는다면 수명이 다 되어 교체 주기 되었다고 판단합니다. \n ");
        // 더 많은 팁 추가 가능


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
