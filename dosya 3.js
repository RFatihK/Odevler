const fs = require("fs");

fs.readFile("ks_200_0", (err, data) => {
  if (err) throw err;

  let tumDosya = data.toString();
  let lines = tumDosya.split("\n");
  let weights = new Array();
  let values = new Array();
  let bag_size;
  let n;

  for (let i = 0; i < lines.length - 1; i++) {
    let line = lines[i];
    let nums = line.split(" ");

    i == 0
      ? (bag_size = parseInt(nums[1]))
      : values.push(parseInt(nums[0])) && weights.push(parseInt(nums[1]));
  }

  n = values.length;

  console.log(
    "Bag size : " +
      bag_size +
      " \nWeights : " +
      weights +
      "\nValues : " +
      values +
      "\nFile Size : " +
      n
  );

  //  0-1 sırt çantası sorunu için alan optimize edilmiş bir DP çözümünü yapan kısım.

  // val[] maksimum depolama

  // wt[] ağırlıkları saklamak içindir
  // n nesne numarası
  // W maksimum çanta kapasitesi
  // nihai sonucu saklamak için mat[2][W+1]
  function KnapSack(val, wt, n, W) {
    // en son sonucu saklayan matris
    let mat = new Array(2);
    for (let i = 0; i < 2; i++) {
      mat[i] = new Array(W + 1);
    }
    for (let i = 0; i < 2; i++) {
      for (let j = 0; j < W + 1; j++) {
        mat[i][j] = 0;
      }
    }

    // tüm öğeleri yinele
    let i = 0;
    while (i < n) {
      // one by one traverse each element
      let j = 0; // traverse all weights j <= W

      // eğer i tek ise, bu şimdiye kadar tek sayıya sahip olduğumuz anlamına gelir
      // eleman sayısı yani sonucu 1'de saklıyoruz
      // indekslenmiş satır
      if (i % 2 != 0) {
        while (++j <= W) {
          // check for each value
          if (wt[i] <= j) {
            // include element
            mat[1][j] = Math.max(val[i] + mat[0][j - wt[i]], mat[0][j]);
          } // exclude element
          else {
            mat[1][j] = mat[0][j];
          }
        }
      }

      // eğer i çift ise şimdiye kadar demektir
      // çift sayıda elemanımız var
      // böylece sonucu 0. dizinlenmiş satırda saklarız
      else {
        while (++j <= W) {
          if (wt[i] <= j) {
            mat[0][j] = Math.max(val[i] + mat[1][j - wt[i]], mat[1][j]);
          } else {
            mat[0][j] = mat[1][j];
          }
        }
      }
      i++;
    }

    // stores the result of Knapsack
    let res = n % 2 != 0 ? mat[0] : mat[1];
    //console.log(res)

    // Return mat[0][W] if n is odd, else mat[1][W]
    return n % 2 != 0 ? mat[0][W] : mat[1][W];
  }

  console.log(KnapSack(values, weights, n, bag_size));

  // for (let i = 0; i < mat.length.length; i++) {
  //     const element = array[i];

  // }
});
