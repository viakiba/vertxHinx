mvn clean package -DskipTests
mkdir output
cp -r ./config ./output/
cp ./target/hinx-1.0-SNAPSHOT.jar ./output/