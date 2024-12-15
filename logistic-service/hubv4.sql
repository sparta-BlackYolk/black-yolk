-- Insert hubs
INSERT INTO p_hub (hub_id, hub_status, hub_name, hub_center, axis_x, axis_y, sido, sigungu, eupmyun, road_name, building_number, zip_code, user_id, created_at, created_by, updated_at, updated_by, deleted_at, deleted_by, is_deleted)
VALUES
    (gen_random_uuid(), 'ACTIVE', '서울 Hub', '서울특별시 센터', 126.978388, 37.566610, '서울특별시', '송파구', NULL, '송파대로', '55', '05843', 'master', CURRENT_TIMESTAMP, 'master', CURRENT_TIMESTAMP, 'master', NULL, NULL, FALSE),
    (gen_random_uuid(), 'ACTIVE', '경기 북부 Hub', '경기 북부 센터', 127.061509, 37.895376, '경기도', '고양시 덕양구', NULL, '권율대로', '570', '10248', 'master', CURRENT_TIMESTAMP, 'master', CURRENT_TIMESTAMP, 'master', NULL, NULL, FALSE),
    (gen_random_uuid(), 'ACTIVE', '경기 남부 Hub', '경기 남부 센터', 127.123789, 37.263573, '경기도', '이천시', NULL, '덕평로', '257-21', '17396', 'master', CURRENT_TIMESTAMP, 'master', CURRENT_TIMESTAMP, 'master', NULL, NULL, FALSE),
    (gen_random_uuid(), 'ACTIVE', '부산 Hub', '부산광역시 센터', 129.075642, 35.179554, '부산광역시', '동구', NULL, '중앙대로', '206', '48748', 'master', CURRENT_TIMESTAMP, 'master', CURRENT_TIMESTAMP, 'master', NULL, NULL, FALSE),
    (gen_random_uuid(), 'ACTIVE', '대구 Hub', '대구광역시 센터', 128.601445, 35.871390, '대구광역시', '북구', NULL, '태평로', '161', '41550', 'master', CURRENT_TIMESTAMP, 'master', CURRENT_TIMESTAMP, 'master', NULL, NULL, FALSE),
    (gen_random_uuid(), 'ACTIVE', '인천 Hub', '인천광역시 센터', 126.705204, 37.456256, '인천광역시', '남동구', NULL, '정각로', '29', '21553', 'master', CURRENT_TIMESTAMP, 'master', CURRENT_TIMESTAMP, 'master', NULL, NULL, FALSE),
    (gen_random_uuid(), 'ACTIVE', '광주 Hub', '광주광역시 센터', 126.851338, 35.160023, '광주광역시', '서구', NULL, '내방로', '111', '61947', 'master', CURRENT_TIMESTAMP, 'master', CURRENT_TIMESTAMP, 'master', NULL, NULL, FALSE),
    (gen_random_uuid(), 'ACTIVE', '대전 Hub', '대전광역시 센터', 127.384548, 36.350461, '대전광역시', '서구', NULL, '둔산로', '100', '35242', 'master', CURRENT_TIMESTAMP, 'master', CURRENT_TIMESTAMP, 'master', NULL, NULL, FALSE),
    (gen_random_uuid(), 'ACTIVE', '울산 Hub', '울산광역시 센터', 129.311299, 35.539777, '울산광역시', '남구', NULL, '중앙로', '201', '44703', 'master', CURRENT_TIMESTAMP, 'master', CURRENT_TIMESTAMP, 'master', NULL, NULL, FALSE),
    (gen_random_uuid(), 'ACTIVE', '세종 Hub', '세종특별자치시 센터', 127.289101, 36.480086, '세종특별자치시', NULL, NULL, '한누리대로', '2130', '30100', 'master', CURRENT_TIMESTAMP, 'master', CURRENT_TIMESTAMP, 'master', NULL, NULL, FALSE),
    (gen_random_uuid(), 'ACTIVE', '강원 Hub', '강원특별자치도 센터', 127.730594, 37.885374, '강원특별자치도', '춘천시', NULL, '중앙로', '1', '24355', 'master', CURRENT_TIMESTAMP, 'master', CURRENT_TIMESTAMP, 'master', NULL, NULL, FALSE),
    (gen_random_uuid(), 'ACTIVE', '충북 Hub', '충청북도 센터', 127.491282, 36.635748, '충청북도', '청주시 상당구', NULL, '상당로', '82', '28527', 'master', CURRENT_TIMESTAMP, 'master', CURRENT_TIMESTAMP, 'master', NULL, NULL, FALSE),
    (gen_random_uuid(), 'ACTIVE', '충남 Hub', '충청남도 센터', 126.705180, 36.518374, '충청남도', '홍성군 홍북읍', NULL, '충남대로', '21', '32229', 'master', CURRENT_TIMESTAMP, 'master', CURRENT_TIMESTAMP, 'master', NULL, NULL, FALSE),
    (gen_random_uuid(), 'ACTIVE', '전북 Hub', '전북특별자치도 센터', 127.108759, 35.821058, '전북특별자치도', '전주시 완산구', NULL, '효자로', '225', '54969', 'master', CURRENT_TIMESTAMP, 'master', CURRENT_TIMESTAMP, 'master', NULL, NULL, FALSE),
    (gen_random_uuid(), 'ACTIVE', '전남 Hub', '전라남도 센터', 126.462919, 34.816111, '전라남도', '무안군 삼향읍', NULL, '오룡길', '1', '58567', 'master', CURRENT_TIMESTAMP, 'master', CURRENT_TIMESTAMP, 'master', NULL, NULL, FALSE),
    (gen_random_uuid(), 'ACTIVE', '경북 Hub', '경상북도 센터', 128.602750, 36.574493, '경상북도', '안동시 풍천면', NULL, '도청대로', '455', '36759', 'master', CURRENT_TIMESTAMP, 'master', CURRENT_TIMESTAMP, 'master', NULL, NULL, FALSE),
    (gen_random_uuid(), 'ACTIVE', '경남 Hub', '경상남도 센터', 128.259623, 35.460773, '경상남도', '창원시 의창구', NULL, '중앙대로', '300', '51430', 'master', CURRENT_TIMESTAMP, 'master', CURRENT_TIMESTAMP, 'master', NULL, NULL, FALSE);


INSERT INTO p_hub_route (hub_route_id, hub_route_status, departure_hub_id, arrival_hub_id, duration, distance, created_at, created_by, updated_at, updated_by, deleted_at, deleted_by, is_deleted)
VALUES
    -- 경기 남부 -> 경기 북부
    (gen_random_uuid(), 'ACTIVE', (SELECT hub_id FROM p_hub WHERE hub_name = '경기 남부 Hub'), (SELECT hub_id FROM p_hub WHERE hub_name = '경기 북부 Hub'), 1800000, 25000, CURRENT_TIMESTAMP, 'master', CURRENT_TIMESTAMP, 'master', NULL, NULL, FALSE),

    -- 경기 남부 -> 서울
    (gen_random_uuid(), 'ACTIVE', (SELECT hub_id FROM p_hub WHERE hub_name = '경기 남부 Hub'), (SELECT hub_id FROM p_hub WHERE hub_name = '서울 Hub'), 2400000, 35000, CURRENT_TIMESTAMP, 'master', CURRENT_TIMESTAMP, 'master', NULL, NULL, FALSE),

    -- 경기 남부 -> 인천
    (gen_random_uuid(), 'ACTIVE', (SELECT hub_id FROM p_hub WHERE hub_name = '경기 남부 Hub'), (SELECT hub_id FROM p_hub WHERE hub_name = '인천 Hub'), 3000000, 40000, CURRENT_TIMESTAMP, 'master', CURRENT_TIMESTAMP, 'master', NULL, NULL, FALSE),

    -- 경기 남부 -> 강원
    (gen_random_uuid(), 'ACTIVE', (SELECT hub_id FROM p_hub WHERE hub_name = '경기 남부 Hub'), (SELECT hub_id FROM p_hub WHERE hub_name = '강원 Hub'), 4200000, 60000, CURRENT_TIMESTAMP, 'master', CURRENT_TIMESTAMP, 'master', NULL, NULL, FALSE),

    -- 경기 남부 -> 경북
    (gen_random_uuid(), 'ACTIVE', (SELECT hub_id FROM p_hub WHERE hub_name = '경기 남부 Hub'), (SELECT hub_id FROM p_hub WHERE hub_name = '경북 Hub'), 7200000, 100000, CURRENT_TIMESTAMP, 'master', CURRENT_TIMESTAMP, 'master', NULL, NULL, FALSE),

    -- 경기 남부 -> 대전
    (gen_random_uuid(), 'ACTIVE', (SELECT hub_id FROM p_hub WHERE hub_name = '경기 남부 Hub'), (SELECT hub_id FROM p_hub WHERE hub_name = '대전 Hub'), 6000000, 80000, CURRENT_TIMESTAMP, 'master', CURRENT_TIMESTAMP, 'master', NULL, NULL, FALSE),

    -- 경기 남부 -> 대구
    (gen_random_uuid(), 'ACTIVE', (SELECT hub_id FROM p_hub WHERE hub_name = '경기 남부 Hub'), (SELECT hub_id FROM p_hub WHERE hub_name = '대구 Hub'), 9000000, 120000, CURRENT_TIMESTAMP, 'master', CURRENT_TIMESTAMP, 'master', NULL, NULL, FALSE),


    -- 서울 Hub -> 경기 남부 Hub
    (gen_random_uuid(), 'ACTIVE', (SELECT hub_id FROM p_hub WHERE hub_name = '서울 Hub'), (SELECT hub_id FROM p_hub WHERE hub_name = '경기 남부 Hub'), 3600000, 40000, CURRENT_TIMESTAMP, 'master', CURRENT_TIMESTAMP, 'master', NULL, NULL, FALSE),


    -- 경기 북부 -> 경기 남부
    (gen_random_uuid(), 'ACTIVE', (SELECT hub_id FROM p_hub WHERE hub_name = '경기 북부 Hub'), (SELECT hub_id FROM p_hub WHERE hub_name = '경기 남부 Hub'), 1800000, 25000, CURRENT_TIMESTAMP, 'master', CURRENT_TIMESTAMP, 'master', NULL, NULL, FALSE),


    -- 부산광역시 -> 대구
    (gen_random_uuid(), 'ACTIVE', (SELECT hub_id FROM p_hub WHERE hub_name = '부산 Hub'), (SELECT hub_id FROM p_hub WHERE hub_name = '대구 Hub'), 4800000, 70000, CURRENT_TIMESTAMP, 'master', CURRENT_TIMESTAMP, 'master', NULL, NULL, FALSE),


    -- 대구 -> 경북
    (gen_random_uuid(), 'ACTIVE', (SELECT hub_id FROM p_hub WHERE hub_name = '대구 Hub'), (SELECT hub_id FROM p_hub WHERE hub_name = '경북 Hub'), 2400000, 35000, CURRENT_TIMESTAMP, 'master', CURRENT_TIMESTAMP, 'master', NULL, NULL, FALSE),

    -- 대구 -> 경남
    (gen_random_uuid(), 'ACTIVE', (SELECT hub_id FROM p_hub WHERE hub_name = '대구 Hub'), (SELECT hub_id FROM p_hub WHERE hub_name = '경남 Hub'), 3600000, 50000, CURRENT_TIMESTAMP, 'master', CURRENT_TIMESTAMP, 'master', NULL, NULL, FALSE),

    -- 대구 -> 부산
    (gen_random_uuid(), 'ACTIVE', (SELECT hub_id FROM p_hub WHERE hub_name = '대구 Hub'), (SELECT hub_id FROM p_hub WHERE hub_name = '부산 Hub'), 5400000, 75000, CURRENT_TIMESTAMP, 'master', CURRENT_TIMESTAMP, 'master', NULL, NULL, FALSE),

    -- 대구 -> 울산
    (gen_random_uuid(), 'ACTIVE', (SELECT hub_id FROM p_hub WHERE hub_name = '대구 Hub'), (SELECT hub_id FROM p_hub WHERE hub_name = '울산 Hub'), 4200000, 60000, CURRENT_TIMESTAMP, 'master', CURRENT_TIMESTAMP, 'master', NULL, NULL, FALSE),

    -- 대구 -> 경기 남부
    (gen_random_uuid(), 'ACTIVE', (SELECT hub_id FROM p_hub WHERE hub_name = '대구 Hub'), (SELECT hub_id FROM p_hub WHERE hub_name = '경기 남부 Hub'), 10800000, 150000, CURRENT_TIMESTAMP, 'master', CURRENT_TIMESTAMP, 'master', NULL, NULL, FALSE),

    -- 대구 -> 대전
    (gen_random_uuid(), 'ACTIVE', (SELECT hub_id FROM p_hub WHERE hub_name = '대구 Hub'), (SELECT hub_id FROM p_hub WHERE hub_name = '대전 Hub'), 12000000, 170000, CURRENT_TIMESTAMP, 'master', CURRENT_TIMESTAMP, 'master', NULL, NULL, FALSE),


    -- 인천 -> 경기 남부
    (gen_random_uuid(), 'ACTIVE', (SELECT hub_id FROM p_hub WHERE hub_name = '인천 Hub'), (SELECT hub_id FROM p_hub WHERE hub_name = '경기 남부 Hub'), 2400000, 35000, CURRENT_TIMESTAMP, 'master', CURRENT_TIMESTAMP, 'master', NULL, NULL, FALSE),


    -- 광주 -> 대전
    (gen_random_uuid(), 'ACTIVE', (SELECT hub_id FROM p_hub WHERE hub_name = '광주 Hub'), (SELECT hub_id FROM p_hub WHERE hub_name = '대전 Hub'), 7200000, 100000, CURRENT_TIMESTAMP, 'master', CURRENT_TIMESTAMP, 'master', NULL, NULL, FALSE),


    -- 대전 -> 충남
    (gen_random_uuid(), 'ACTIVE', (SELECT hub_id FROM p_hub WHERE hub_name = '대전 Hub'), (SELECT hub_id FROM p_hub WHERE hub_name = '충남 Hub'), 1800000, 20000, CURRENT_TIMESTAMP, 'master', CURRENT_TIMESTAMP, 'master', NULL, NULL, FALSE),

    -- 대전 -> 충북
    (gen_random_uuid(), 'ACTIVE', (SELECT hub_id FROM p_hub WHERE hub_name = '대전 Hub'), (SELECT hub_id FROM p_hub WHERE hub_name = '충북 Hub'), 2400000, 30000, CURRENT_TIMESTAMP, 'master', CURRENT_TIMESTAMP, 'master', NULL, NULL, FALSE),

    -- 대전 -> 세종
    (gen_random_uuid(), 'ACTIVE', (SELECT hub_id FROM p_hub WHERE hub_name = '대전 Hub'), (SELECT hub_id FROM p_hub WHERE hub_name = '세종 Hub'), 1200000, 15000, CURRENT_TIMESTAMP, 'master', CURRENT_TIMESTAMP, 'master', NULL, NULL, FALSE),

    -- 대전 -> 전북
    (gen_random_uuid(), 'ACTIVE', (SELECT hub_id FROM p_hub WHERE hub_name = '대전 Hub'), (SELECT hub_id FROM p_hub WHERE hub_name = '전북 Hub'), 3600000, 50000, CURRENT_TIMESTAMP, 'master', CURRENT_TIMESTAMP, 'master', NULL, NULL, FALSE),

    -- 대전 -> 광주
    (gen_random_uuid(), 'ACTIVE', (SELECT hub_id FROM p_hub WHERE hub_name = '대전 Hub'), (SELECT hub_id FROM p_hub WHERE hub_name = '광주 Hub'), 7200000, 100000, CURRENT_TIMESTAMP, 'master', CURRENT_TIMESTAMP, 'master', NULL, NULL, FALSE),

    -- 대전 -> 전남
    (gen_random_uuid(), 'ACTIVE', (SELECT hub_id FROM p_hub WHERE hub_name = '대전 Hub'), (SELECT hub_id FROM p_hub WHERE hub_name = '전남 Hub'), 5400000, 75000, CURRENT_TIMESTAMP, 'master', CURRENT_TIMESTAMP, 'master', NULL, NULL, FALSE),

    -- 대전 -> 경기 남부
    (gen_random_uuid(), 'ACTIVE', (SELECT hub_id FROM p_hub WHERE hub_name = '대전 Hub'), (SELECT hub_id FROM p_hub WHERE hub_name = '경기 남부 Hub'), 5400000, 80000, CURRENT_TIMESTAMP, 'master', CURRENT_TIMESTAMP, 'master', NULL, NULL, FALSE),

    -- 대전 -> 대구
    (gen_random_uuid(), 'ACTIVE', (SELECT hub_id FROM p_hub WHERE hub_name = '대전 Hub'), (SELECT hub_id FROM p_hub WHERE hub_name = '대구 Hub'), 8400000, 120000, CURRENT_TIMESTAMP, 'master', CURRENT_TIMESTAMP, 'master', NULL, NULL, FALSE),


    -- 울산 -> 대구
    (gen_random_uuid(), 'ACTIVE', (SELECT hub_id FROM p_hub WHERE hub_name = '울산 Hub'), (SELECT hub_id FROM p_hub WHERE hub_name = '대구 Hub'), 4200000, 60000, CURRENT_TIMESTAMP, 'master', CURRENT_TIMESTAMP, 'master', NULL, NULL, FALSE),


    -- 세종 -> 대전
    (gen_random_uuid(), 'ACTIVE', (SELECT hub_id FROM p_hub WHERE hub_name = '세종 Hub'), (SELECT hub_id FROM p_hub WHERE hub_name = '대전 Hub'), 1200000, 15000, CURRENT_TIMESTAMP, 'master', CURRENT_TIMESTAMP, 'master', NULL, NULL, FALSE),


    -- 강원 -> 경기 남부
    (gen_random_uuid(), 'ACTIVE', (SELECT hub_id FROM p_hub WHERE hub_name = '강원 Hub'), (SELECT hub_id FROM p_hub WHERE hub_name = '경기 남부 Hub'), 4200000, 60000, CURRENT_TIMESTAMP, 'master', CURRENT_TIMESTAMP, 'master', NULL, NULL, FALSE),


    -- 충북 -> 대전
    (gen_random_uuid(), 'ACTIVE', (SELECT hub_id FROM p_hub WHERE hub_name = '충북 Hub'), (SELECT hub_id FROM p_hub WHERE hub_name = '대전 Hub'), 2400000, 30000, CURRENT_TIMESTAMP, 'master', CURRENT_TIMESTAMP, 'master', NULL, NULL, FALSE),


    -- 충남 -> 대전
    (gen_random_uuid(), 'ACTIVE', (SELECT hub_id FROM p_hub WHERE hub_name = '충남 Hub'), (SELECT hub_id FROM p_hub WHERE hub_name = '대전 Hub'), 2400000, 30000, CURRENT_TIMESTAMP, 'master', CURRENT_TIMESTAMP, 'master', NULL, NULL, FALSE),


    -- 전북 -> 대전
    (gen_random_uuid(), 'ACTIVE', (SELECT hub_id FROM p_hub WHERE hub_name = '전북 Hub'), (SELECT hub_id FROM p_hub WHERE hub_name = '대전 Hub'), 2400000, 30000, CURRENT_TIMESTAMP, 'master', CURRENT_TIMESTAMP, 'master', NULL, NULL, FALSE),


    -- 전남 -> 대전
    (gen_random_uuid(), 'ACTIVE', (SELECT hub_id FROM p_hub WHERE hub_name = '전남 Hub'), (SELECT hub_id FROM p_hub WHERE hub_name = '대전 Hub'), 2400000, 30000, CURRENT_TIMESTAMP, 'master', CURRENT_TIMESTAMP, 'master', NULL, NULL, FALSE),


    -- 경북 -> 경기 남부
    (gen_random_uuid(), 'ACTIVE', (SELECT hub_id FROM p_hub WHERE hub_name = '경북 Hub'), (SELECT hub_id FROM p_hub WHERE hub_name = '경기 남부 Hub'), 7200000, 100000, CURRENT_TIMESTAMP, 'master', CURRENT_TIMESTAMP, 'master', NULL, NULL, FALSE),

    -- 경북 -> 대구
    (gen_random_uuid(), 'ACTIVE', (SELECT hub_id FROM p_hub WHERE hub_name = '경북 Hub'), (SELECT hub_id FROM p_hub WHERE hub_name = '대구 Hub'), 2400000, 35000, CURRENT_TIMESTAMP, 'master', CURRENT_TIMESTAMP, 'master', NULL, NULL, FALSE),


    -- 경남 -> 대구
    (gen_random_uuid(), 'ACTIVE', (SELECT hub_id FROM p_hub WHERE hub_name = '경남 Hub'), (SELECT hub_id FROM p_hub WHERE hub_name = '대구 Hub'), 2400000, 35000, CURRENT_TIMESTAMP, 'master', CURRENT_TIMESTAMP, 'master', NULL, NULL, FALSE);
