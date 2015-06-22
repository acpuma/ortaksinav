jQuery(document).ready(function($) {
    $('.openlist').click(function () {
        var xr = $(this).attr('rel');
        xr = xr.replace('o', 's');
        $('#' + xr).slideToggle(200);
    });
    $('.count_input').focus(function () {
        var vl = $(this).val();
        if (vl == 0) {
            $(this).val('');
        }
    });
    $('.count_input').blur(function () {
        var vl = $(this).val();
        if (vl == '' || isNaN(parseInt(vl))) {
            $(this).val('0');
            SumAll();
        }
    });
    $('.count_input').keyup(function () {
        var xc = $('.count_input').index(this);
        SumLine(xc);
        var cat = $('.line_sum:eq(' + xc + ')').attr('rel');
        SumCat(cat);
        cat = parseInt(cat.replace('gr', '')) - 2;
        $('.count_input_gr:eq(' + cat + ')').val(0);
    });
    $('.count_input_gr').focus(function () {
        var vl = $(this).val();
        if (vl == 0) {
            $(this).val('');
        }
    });
    $('.count_input_gr').blur(function () {
        var vl = $(this).val();
        if (vl == '' || isNaN(parseInt(vl))) {
            $(this).val('0');
        }
    });
    $('.count_input_gr').keyup(function () {
        var gr = $(this).attr('rel');
        var tv = $(this).val();
        var ln = $('.' + gr).length;
        for (i = 0; i < ln; i++) {
            $('.' + gr + ':eq(' + i + ')').val(tv);
            var xc = $('.count_input').index($('.' + gr + ':eq(' + i + ')'));
            SumLine(xc);
        }
        var cat = $('.line_sum:eq(' + xc + ')').attr('rel');
        SumCat(cat);
    });
    $('#ys_alt_min').focus(function () {
        var vl = $(this).val();
        if (vl == 0) {
            $(this).val('');
        }
    });
    $('#ys_alt_min').blur(function () {
        var vl = $(this).val();
        if (vl == '' || isNaN(parseInt(vl))) {
            $(this).val('0');
        }
    });
    $('#ys_alt_min').keyup(function () {
        SumRes();
    });

    $('.ys_op').click(function () {
        var xzname = $('#ys_ext_name').val();
        var xzaddr = $('#ys_ext_addr').val();
        var xztel = $('#ys_ext_phone').val();
        var xzmail = $('#ys_ext_mail').val();

        if (xzname == '' || xzaddr == '' || xztel == '' || xzmail == '') {
            alert('Lütfen isim, adres, telefon ve e-posta adresinizi belirtin');
        }
        /*
        else {
            $('#ys').slideUp(300);
            YsList(status);
            $('html, body').animate({scrollTop: 0}, 0);
        }
        */
    });


    $.fn.sumValues = function () {
        var sum = 0;
        this.each(function () {
            if ($(this).is(':input')) {
                var val = $(this).val();
            } else {
                var val = $(this).text();
            }
            sum += parseFloat(('0' + val).replace(/[^0-9-\.]/g, ''), 10);
        });
        return sum;
    };

    function SumLine(xc) {
        var cn = parseInt($('.count_input:eq(' + xc + ')').val(), 10);
        var pr = parseFloat($('.pr_field:eq(' + xc + ')').html());
        var rs = (cn * pr).toFixed(2);
        if (rs == '' || isNaN(parseFloat(rs))) {
            rs = 0;
        }
        $('.line_sum:eq(' + xc + ')').html(rs + ' TL');
    }

    function SumCat(cat) {
        $('#' + cat).html($('.line_' + cat).sumValues().toFixed(2) + ' TL');
        SumAll();
    }

    function SumAll() {
        $('#sumcn').html($('.count_input').sumValues());
        $('#sumpr').html($('.line_sum').sumValues().toFixed(2) + ' TL');
        SumRes();
    }

    function SumRes() {
        var io = parseInt($('#ys_alt_min').val());
        var sm = parseFloat($('#sumpr').html());
        var rs = ((sm * io) / 100).toFixed(2);
        $('#io_rs').html(rs + ' TL');
        var tt = (sm - rs).toFixed(2);
        $('#ys_tt').html(tt + ' TL');
        var kd = (tt * 0.08).toFixed(2);
        $('#ys_kd').html(kd + ' TL');
        var st = parseFloat(tt).toFixed(2);
        $('#ys_st').html(st + ' TL');
    }

    function YsList(status) {
        var ln = $('.book_names').length;
        var d = new Date();
        var month = d.getMonth() + 1;
        var cname = $('#ys_ext_name').val();
        var caddr = $('#ys_ext_addr').val();
        var ctel = $('#ys_ext_phone').val();
        var cmail = $('#ys_ext_mail').val();

        var tx = 'Ad: <b>' + cname + '</b><br />Adres: <b>' + caddr + '</b><br />Telefon: <b>' + ctel + '</b><br />E-posta: <b>' + cmail + '</b><br /><br /><span class="sl_title">Sipariş Listesi</span><span class="sl_date">Sipariş Tarihi: ' + d.getDate() + '.' + month + '.' + d.getFullYear() + '</span><br /><div class="book_list toplabel"><div class="book_name">Kitap Adı</div><div class="book_count">Adet</div><div class="book_price">Tutar</div><div class="clear"></div></div>';

        var mtx = '<b>Sipariş Listesi</b><br />Sipariş Tarihi: ' + d.getDate() + '.' + month + '.' + d.getFullYear() + '<br /><br />';
        var bl;
        var ti = 0;
        var line_class = 0;
        var dclass;
        for (i = 0; i < ln; i++) {
            cn = $('.count_input:eq(' + i + ')').val();
            if (cn > 0) {
                bn = $('.book_names:eq(' + i + ')').html();
                bn.replace("\\", "");
                pr = parseFloat($('.pr_field:eq(' + i + ')').html());
                rs = (cn * pr).toFixed(2);
                if (ti % 2 == 0) {
                    clt = ' cleax';
                }
                else {
                    clt = ' dright';
                }

                if (i % 2 == 0) {
                    bl = 'fff';
                }
                else {
                    bl = 'ccc';
                }

                trs = '<div class="book_list' + clt + '" style="border-bottom: 1px solid #333; height: 24px; background: #' + bl + '"><div class="book_name">' + bn + '</div><div class="book_count">' + cn + '</div><div class="book_price">' + rs + ' TL</div></div>';

                if (line_class % 2 == 0) {
                    dclass = 'dwh';
                } else {
                    dclass = 'dgr';
                }
                line_class = parseInt(line_class) + 1;
                mtx = mtx + '<div class="' + dclass + '"><div class="xxbn">' + bn + '</div><div class="xxcn">[' + cn + ']</div><div class="xxpr">' + rs + ' TL</div></div>';
                ti = ti + 1;
            }
            else {
                trs = '';
            }
            tx = tx + trs;
        }
        var cn = $('#sumcn').html();
        var sm = parseFloat($('#sumpr').html()).toFixed(2);
        var io = $('#ys_alt_min').val();
        var ior = parseFloat($('#io_rs').html()).toFixed(2);
        /*var kd = parseFloat($('#ys_kd').html()).toFixed(2);
         var tt = parseFloat($('#ys_tt').html()).toFixed(2);*/
        var st = parseFloat($('#ys_st').html()).toFixed(2);

        var tks = parseInt($('#ys_taksit').val());
        var tkg = parseInt($('#ys_taksit_gun').val());
        var tk = '';

        if (tks == 1) {
            month = d.getMonth() + 1;
            tk = d.getDate() + '.' + month + '.' + d.getFullYear();
        }
        else {
            if (tkg < d.getDate()) {
                m1 = d.getMonth() + 2;
            }
            else {
                m1 = d.getMonth() + 1;
            }
            var y = d.getFullYear();
            var tkx = (parseInt(st) / tks).toFixed(2);
            for (i = 0; i < tks; i++) {
                if (i + 1 == tks) {
                    tkx = (st - ((tks - 1) * tkx)).toFixed(2);
                }
                tk = tk + ' ' + tkg + '.' + m1 + '.' + y + ' ' + tkx + ' TL<br />';
                m1++;
                if (m1 > 12) {
                    m1 = 1;
                    y = y + 1;
                }
            }
        }
        ttl = tx;

        /*<!--<div class="sublabel"><b>Toplam Tutar</b></div><div class="subinfo">'+tt+' TL</div><div class="sublabel"><b>KDV (%8)</b></div><div class="subinfo">'+kd+' TL</div>-->
         <b>Toplam Tutar</b> '+tt+' TL<br /><b>KDV (%8)</b> '+kd+' TL<br />
         */

        var pres = '<div class="clear" style="height: 45px;"></div><div class="sub_block"><div class="sublabel"><b>Sipariş Adedi</b></div><div class="subinfo">' + cn + '</div><div class="sublabel"><b>Genel Toplam</b></div><div class="subinfo">' + sm + ' TL</div><div class="sublabel"><b>İskonto (%' + io + ')</b></div><div class="subinfo">' + ior + ' TL</div><div class="sublabel"><b>Sipariş Tutarı</b></div><div class="subinfo">' + st + ' TL</div></div><div class="sub_block"><b>Ödeme Tarihleri</b><br />' + tk + '</div>';
        tx = tx + pres;
        ttl = ttl + pres + '<div class="clear" style="height: 15px;"></div>';
        tx = tx + '<div class="clear" style="height: 15px;"></div><br /><input type="submit" value="Siparişi Onaylıyorum" id="ys_ok" class="bt_green" /> <input type="submit" value="Geri Dön" id="ys_rt" class="bt_orange" />';

        mtx = mtx + '<br /><br /><b>Sipariş Adedi</b> ' + cn + '<br /><b>Genel Toplam</b> ' + sm + ' TL<br /><b>İskonto (%' + io + ')</b> ' + ior + ' TL<br /><b>Sipariş Tutarı</b> ' + st + ' TL<br /><b>Ödeme Tarihleri</b><br />' + tk + '';

        $('#ys_res').html(tx);
        $('#ys_rt').click(function () {
            $('#ys').slideDown(300);
            $('#ys_res').html('');
        });
        $('#ys_ok').click(function () {
            window.print();
            var zx = 'Ad : ' + $('#ys_ext_name').val() + '<br />Adres : ' + $('#ys_ext_addr').val() + '<br />Telefon : ' + $('#ys_ext_phone').val() + '<br />E-posta : ' + $('#ys_ext_mail').val();
            $.post('ajax.php?x=send_order', {tx: mtx, cn: cn, sm: st, zx: zx}, function (data) {
                $('#ys_res').html(data);
            });
        });
    }
});