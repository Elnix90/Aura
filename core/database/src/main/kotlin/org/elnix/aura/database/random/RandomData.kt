package org.elnix.aura.database.random

/**
 * Long bundled lists used to generate random identity values.
 *
 * Every list intentionally holds hundreds of generic, public domain style
 * entries so the generated values stay varied and look realistic enough to be
 * handed out to shops.
 */
public object RandomData {

    public val firstNames: List<String> = listOf(
        "James", "Mary", "Robert", "Patricia", "John", "Jennifer", "Michael", "Linda", "David",
        "Elizabeth", "William", "Barbara", "Richard", "Susan", "Joseph", "Jessica", "Thomas",
        "Sarah", "Charles", "Karen", "Christopher", "Lisa", "Daniel", "Nancy", "Matthew",
        "Betty", "Anthony", "Margaret", "Mark", "Sandra", "Donald", "Ashley", "Steven", "Kimberly",
        "Paul", "Emily", "Andrew", "Donna", "Joshua", "Michelle", "Kenneth", "Carol", "Kevin",
        "Amanda", "Brian", "Dorothy", "George", "Melissa", "Timothy", "Deborah", "Ronald",
        "Stephanie", "Edward", "Rebecca", "Jason", "Sharon", "Jeffrey", "Laura", "Ryan",
        "Cynthia", "Jacob", "Kathleen", "Gary", "Amy", "Nicholas", "Angela", "Eric", "Shirley",
        "Jonathan", "Anna", "Stephen", "Brenda", "Larry", "Pamela", "Justin", "Emma", "Scott",
        "Nicole", "Brandon", "Helen", "Benjamin", "Samantha", "Samuel", "Katherine", "Gregory",
        "Christine", "Alexander", "Debra", "Patrick", "Rachel", "Frank", "Carolyn", "Jack",
        "Janet", "Henry", "Catherine", "Kyle", "Maria", "Walter", "Heather", "Ethan", "Diane",
        "Oliver", "Olivia", "Liam", "Ava", "Noah", "Sophia", "Logan", "Chloe", "Lucas", "Mia",
        "Mohammed", "Fatima", "Yuval", "Noa", "Itai", "Tamar", "Eitan", "Shira", "Ori", "Michal",
        "Amir", "Roni", "Gal", "Nadav", "Tal", "Yael", "Yosef", "Hila", "Avi", "Dana", "Yaron",
        "Lior", "Omer", "Efrat", "Matan", "Sarit", "Ido", "Naomi", "Guy", "Maya", "Oren", "Liat",
    )

    public val surnames: List<String> = listOf(
        "Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis",
        "Rodriguez", "Martinez", "Hernandez", "Lopez", "Gonzalez", "Wilson", "Anderson",
        "Thomas", "Taylor", "Moore", "Jackson", "Martin", "Lee", "Perez", "Thompson", "White",
        "Harris", "Sanchez", "Clark", "Ramirez", "Lewis", "Robinson", "Walker", "Young",
        "Allen", "King", "Wright", "Scott", "Torres", "Nguyen", "Hill", "Flores", "Green",
        "Adams", "Nelson", "Baker", "Hall", "Rivera", "Campbell", "Mitchell", "Carter",
        "Roberts", "Gomez", "Phillips", "Evans", "Turner", "Diaz", "Parker", "Cruz",
        "Edwards", "Collins", "Reyes", "Stewart", "Morris", "Morales", "Murphy", "Cook",
        "Rogers", "Gutierrez", "Ortiz", "Morgan", "Cooper", "Peterson", "Bailey", "Reed",
        "Kelly", "Howard", "Ramos", "Kim", "Cox", "Ward", "Richardson", "Watson", "Brooks",
        "Chavez", "Wood", "James", "Bennett", "Gray", "Mendoza", "Ruiz", "Hughes", "Price",
        "Alvarez", "Castillo", "Sanders", "Patel", "Myers", "Long", "Ross", "Foster", "Jimenez",
        "Levy", "Cohen", "Abramson", "Katz", "Shapiro", "Friedman", "Goldberg", "Rosenberg",
        "Weiss", "Mizrahi", "Peretz", "Ohana", "Ben-David", "Dayan", "Azoulay", "Biton",
        "Yosef", "Haddad", "Shalom", "Nakamura", "Takahashi", "Suzuki", "Tanaka", "Watanabe",
        "Ivanov", "Petrov", "Sidorov", "Kowalski", "Novak", "Horvat", "Jovanovic", "Popov",
        "Dubois", "Moreau", "Laurent", "Lefebvre", "Garcia", "Fernandez", "Silva", "Sousa",
    )

    public val streets: List<String> = listOf(
        "Maple Street", "High Street", "Park Avenue", "Oak Avenue", "Main Street", "Church Street",
        "King Street", "Queen Street", "Market Street", "Station Road", "Mill Lane", "Grove Road",
        "School Lane", "Hill Street", "Green Lane", "New Street", "Victoria Road", "Water Street",
        "Mill Street", "Bridge Street", "Park Lane", "Broad Street", "Garden Street", "North Street",
        "South Street", "East Street", "West Street", "London Road", "Springfield Road", "Cedar Street",
        "Elm Street", "Pine Street", "Walnut Street", "Birch Street", "Willow Lane", "Sunset Boulevard",
        "Sunrise Avenue", "Lakeview Drive", "Riverside Drive", "Hillside Avenue", "Meadow Lane",
        "Fairview Avenue", "Chestnut Street", "Laurel Avenue", "Hollywood Boulevard", "Central Avenue",
        "Lincoln Street", "Washington Avenue", "Adams Street", "Jefferson Avenue", "Madison Avenue",
        "Monroe Street", "Franklin Street", "Columbus Avenue", "Liberty Street", "Union Street",
        "Pearl Street", "Church Avenue", "Court Street", "State Street", "Elmwood Avenue",
        "Birchwood Road", "Cedar Lane", "Dogwood Drive", "Heron Drive", "Ivy Lane", "Juniper Road",
        "Maplewood Drive", "Oakwood Lane", "Poplar Avenue", "Quarry Road", "Rosewood Drive",
        "Sycamore Street", "Tulip Lane", "Vine Street", "Windsor Road", "Hazel Lane", "Beech Street",
        "Ash Grove", "Acacia Avenue", "Amethyst Street", "Beryl Road", "Coral Street", "Diamond Court",
        "Emerald Drive", "Fern Road", "Garnet Street", "Heather Road", "Jade Avenue", "Jasmine Lane",
        "Kepler Street", "Lavender Way", "Magnolia Drive", "Nutmeg Lane", "Orchard Street",
        "Primrose Path", "Quince Street", "Sage Lane", "Topaz Court", "Upland Road", "Violet Street",
        "Walnut Lane", "Xerxes Street", "Yarrow Lane", "Zinnia Avenue", "Second Avenue", "Fifth Street",
        "Tenth Avenue", "Twenty-first Street", "Blueberry Lane", "Cherry Street", "Daisy Lane",
    )

    public val cities: List<String> = listOf(
        "New York", "Los Angeles", "Chicago", "Houston", "Phoenix", "Philadelphia", "San Antonio",
        "San Diego", "Dallas", "San Francisco", "Boston", "Seattle", "Denver", "Austin", "Miami",
        "Atlanta", "Portland", "Detroit", "Minneapolis", "Nashville", "Baltimore", "Louisville",
        "Milwaukee", "Albuquerque", "Tucson", "Fresno", "Sacramento", "Kansas City", "Cleveland",
        "Omaha", "Honolulu", "Anchorage", "London", "Manchester", "Birmingham", "Leeds", "Glasgow",
        "Liverpool", "Bristol", "Sheffield", "Edinburgh", "Cardiff", "Belfast", "Dublin", "Cork",
        "Paris", "Marseille", "Lyon", "Toulouse", "Nice", "Nantes", "Strasbourg", "Bordeaux",
        "Lille", "Berlin", "Hamburg", "Munich", "Cologne", "Frankfurt", "Stuttgart", "Dortmund",
        "Dusseldorf", "Bremen", "Madrid", "Barcelona", "Valencia", "Seville", "Zaragoza",
        "Malaga", "Milan", "Rome", "Naples", "Turin", "Palermo", "Genoa", "Bologna", "Florence",
        "Amsterdam", "Rotterdam", "The Hague", "Utrecht", "Brussels", "Antwerp", "Ghent",
        "Zurich", "Geneva", "Basel", "Bern", "Vienna", "Salzburg", "Graz", "Lisbon", "Porto",
        "Stockholm", "Gothenburg", "Malmo", "Oslo", "Bergen", "Copenhagen", "Aarhus", "Helsinki",
        "Espoo", "Tampere", "Warsaw", "Krakow", "Lodz", "Wroclaw", "Poznan", "Prague", "Brno",
        "Ostrava", "Budapest", "Debrecen", "Athens", "Thessaloniki", "Istanbul", "Ankara",
        "Izmir", "Moscow", "Saint Petersburg", "Kyiv", "Lviv", "Tel Aviv", "Jerusalem",
        "Haifa", "Mumbai", "Delhi", "Bangalore", "Chennai", "Kolkata", "Hyderabad", "Sydney",
        "Melbourne", "Brisbane", "Perth", "Adelaide", "Auckland", "Wellington", "Christchurch",
        "Tokyo", "Osaka", "Kyoto", "Nagoya", "Sapporo", "Seoul", "Busan", "Beijing", "Shanghai",
        "Guangzhou", "Shenzhen", "Hong Kong", "Taipei", "Singapore", "Kuala Lumpur", "Bangkok",
        "Jakarta", "Manila", "Ho Chi Minh City", "Hanoi", "Islamabad", "Karachi", "Dubai",
        "Abu Dhabi", "Riyadh", "Jeddah", "Cairo", "Alexandria", "Johannesburg", "Cape Town",
        "Durban", "Lagos", "Nairobi", "Addis Ababa", "Casablanca", "Tunis", "Algiers",
        "Mexico City", "Guadalajara", "Monterrey", "Sao Paulo", "Rio de Janeiro", "Brasilia",
        "Buenos Aires", "Cordoba", "Santiago", "Lima", "Bogota", "Medellin", "Caracas",
        "Montevideo", "Asuncion", "Quito", "Guayaquil", "Panama City", "San Jose", "Havana",
    )

    public val countries: List<String> = listOf(
        "United States", "Canada", "United Kingdom", "Ireland", "France", "Germany", "Spain",
        "Portugal", "Italy", "Netherlands", "Belgium", "Luxembourg", "Switzerland", "Austria",
        "Sweden", "Norway", "Denmark", "Finland", "Iceland", "Poland", "Czech Republic",
        "Slovakia", "Hungary", "Romania", "Bulgaria", "Greece", "Turkey", "Cyprus", "Malta",
        "Croatia", "Slovenia", "Serbia", "Bosnia and Herzegovina", "North Macedonia", "Albania",
        "Ukraine", "Belarus", "Lithuania", "Latvia", "Estonia", "Russia", "Israel", "Jordan",
        "Lebanon", "Saudi Arabia", "United Arab Emirates", "Qatar", "Kuwait", "Bahrain", "Oman",
        "Iran", "Iraq", "Egypt", "Morocco", "Algeria", "Tunisia", "Libya", "South Africa",
        "Nigeria", "Kenya", "Ethiopia", "Ghana", "Senegal", "Tanzania", "Uganda", "Zimbabwe",
        "Namibia", "Botswana", "India", "Pakistan", "Bangladesh", "Sri Lanka", "Nepal",
        "China", "Japan", "South Korea", "North Korea", "Taiwan", "Vietnam", "Thailand",
        "Malaysia", "Singapore", "Indonesia", "Philippines", "Myanmar", "Cambodia", "Laos",
        "Mongolia", "Australia", "New Zealand", "Fiji", "Brazil", "Argentina", "Mexico",
        "Chile", "Peru", "Colombia", "Venezuela", "Ecuador", "Uruguay", "Paraguay", "Bolivia",
        "Guatemala", "Costa Rica", "Panama", "Cuba", "Dominican Republic", "Jamaica",
    )

    public val emailDomains: List<String> = listOf(
        "gmail.com", "yahoo.com", "outlook.com", "hotmail.com", "proton.me", "protonmail.com",
        "icloud.com", "aol.com", "mail.com", "gmx.com", "zoho.com", "yandex.com", "fastmail.com",
        "hey.com", "tutanota.com", "disroot.org", "posteo.de", "mailfence.com", "inbox.com",
        "lycos.com", "seznam.cz", "wp.pl", "libero.it", "laposte.net", "rambler.ru", "t-online.de",
    )

    public val labels: List<String> = listOf(
        "Grocery store", "Supermarket", "Bakery", "Coffee shop", "Pharmacy", "Bookstore",
        "Electronics shop", "Hardware store", "Clothing store", "Shoe store", "Restaurant",
        "Pizzeria", "Pet shop", "Flower shop", "Gym", "Hair salon", "Barber shop", "Car repair",
        "Gas station", "Dentist", "Optician", "Bank branch", "Post office", "Jewelry store",
        "Toy store", "Furniture store", "Garden center", "Wine shop", "Bike rental",
        "Dry cleaner", "Tailor", "Travel agency", "Mobile operator store",
    )



    // FUUUUUCK its too annoying so I'll keep only french. fuck y'all if you want anything else
//    public val phoneCountries: List<PhoneCountry> = listOf(
//        PhoneCountry("US", R.string.country_united_states, "1", 10, "3 3 4", "\uD83C\uDDFA\uD83C\uDDF8"),
//        PhoneCountry("CA", R.string.country_canada, "1", 10, "3 3 4", "\uD83C\uDDE8\uD83C\uDDE6"),
//        PhoneCountry("GB", R.string.country_united_kingdom, "44", 10, "4 3 3", "\uD83C\uDDEC\uD83C\uDDE7"),
//        PhoneCountry("IE", R.string.country_ireland, "353", 9, "2 3 4", "\uD83C\uDDEE\uD83C\uDDEA"),
//        PhoneCountry("FR", R.string.country_france, "33", 9, "1 2 2 2 2", "\uD83C\uDDEB\uD83C\uDDF7"),
//        PhoneCountry("DE", R.string.country_germany, "49", 11, "2 3 3 3", "\uD83C\uDDE9\uD83C\uDDEA"),
//        PhoneCountry("ES", R.string.country_spain, "34", 9, "3 3 3", "\uD83C\uDDEA\uD83C\uDDF8"),
//        PhoneCountry("IT", R.string.country_italy, "39", 10, "3 3 4", "\uD83C\uDDEE\uD83C\uDDF9"),
//        PhoneCountry("PT", R.string.country_portugal, "351", 9, "3 3 3", "\uD83C\uDDF5\uD83C\uDDF9"),
//        PhoneCountry("NL", R.string.country_netherlands, "31", 9, "3 3 3", "\uD83C\uDDF3\uD83C\uDDF1"),
//        PhoneCountry("BE", R.string.country_belgium, "32", 9, "3 3 3", "\uD83C\uDDE7\uD83C\uDDEA"),
//        PhoneCountry("CH", R.string.country_switzerland, "41", 9, "2 3 4", "\uD83C\uDDE8\uD83C\uDDED"),
//        PhoneCountry("AT", R.string.country_austria, "43", 10, "3 3 4", "\uD83C\uDDE6\uD83C\uDDF9"),
//        PhoneCountry("SE", R.string.country_sweden, "46", 9, "3 3 3", "\uD83C\uDDF8\uD83C\uDDEA"),
//        PhoneCountry("NO", R.string.country_norway, "47", 8, "2 3 3", "\uD83C\uDDF3\uD83C\uDDF4"),
//        PhoneCountry("DK", R.string.country_denmark, "45", 8, "2 2 2 2", "\uD83C\uDDE9\uD83C\uDDF0"),
//        PhoneCountry("FI", R.string.country_finland, "358", 9, "2 3 4", "\uD83C\uDDEB\uD83C\uDDEE"),
//        PhoneCountry("PL", R.string.country_poland, "48", 9, "3 3 3", "\uD83C\uDDF5\uD83C\uDDF1"),
//        PhoneCountry("CZ", R.string.country_czech_republic, "420", 9, "3 3 3", "\uD83C\uDDE8\uD83C\uDDFF"),
//        PhoneCountry("GR", R.string.country_greece, "30", 10, "3 3 4", "\uD83C\uDDEC\uD83C\uDDF7"),
//        PhoneCountry("TR", R.string.country_turkey, "90", 10, "3 3 4", "\uD83C\uDDF9\uD83C\uDDF7"),
//        PhoneCountry("RU", R.string.country_russia, "7", 10, "3 3 4", "\uD83C\uDDF7\uD83C\uDDFA"),
//        PhoneCountry("UA", R.string.country_ukraine, "380", 9, "2 3 4", "\uD83C\uDDFA\uD83C\uDDE6"),
//        PhoneCountry("IL", R.string.country_israel, "972", 9, "2 3 4", "\uD83C\uDDEE\uD83C\uDDF1"),
//        PhoneCountry("IN", R.string.country_india, "91", 10, "3 3 4", "\uD83C\uDDEE\uD83C\uDDF3"),
//        PhoneCountry("AU", R.string.country_australia, "61", 9, "2 3 4", "\uD83C\uDDE6\uD83C\uDDFA"),
//        PhoneCountry("NZ", R.string.country_new_zealand, "64", 8, "2 3 3", "\uD83C\uDDF3\uD83C\uDDFF"),
//        PhoneCountry("JP", R.string.country_japan, "81", 10, "3 3 4", "\uD83C\uDDEF\uD83C\uDDF5"),
//        PhoneCountry("KR", R.string.country_south_korea, "82", 9, "3 3 3", "\uD83C\uDDF0\uD83C\uDDF7"),
//        PhoneCountry("CN", R.string.country_china, "86", 11, "3 4 4", "\uD83C\uDDE8\uD83C\uDDF3"),
//        PhoneCountry("BR", R.string.country_brazil, "55", 11, "2 5 4", "\uD83C\uDDE7\uD83C\uDDF7"),
//        PhoneCountry("MX", R.string.country_mexico, "52", 10, "3 3 4", "\uD83C\uDDF2\uD83C\uDDFD"),
//        PhoneCountry("AR", R.string.country_argentina, "54", 10, "3 3 4", "\uD83C\uDDE6\uD83C\uDDF7"),
//        PhoneCountry("ZA", R.string.country_south_africa, "27", 9, "3 3 3", "\uD83C\uDDFF\uD83C\uDDE6"),
//        PhoneCountry("EG", R.string.country_egypt, "20", 10, "3 3 4", "\uD83C\uDDEA\uD83C\uDDEC"),
//        PhoneCountry("NG", R.string.country_nigeria, "234", 10, "3 3 4", "\uD83C\uDDF3\uD83C\uDDEC"),
//        PhoneCountry("SA", R.string.country_saudi_arabia, "966", 9, "2 3 4", "\uD83C\uDDF8\uD83C\uDDE6"),
//        PhoneCountry("AE", R.string.country_united_arab_emirates, "971", 9, "2 3 4", "\uD83C\uDDE6\uD83C\uDDEA"),
//        PhoneCountry("SG", R.string.country_singapore, "65", 8, "4 4", "\uD83C\uDDF8\uD83C\uDDEC"),
//        PhoneCountry("HK", R.string.country_hong_kong, "852", 8, "4 4", "\uD83C\uDDED\uD83C\uDDF0"),
//    )
}
